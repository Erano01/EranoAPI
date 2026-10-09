package me.erano.com.api.cluster.redis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.erano.com.api.cluster.ArenaStatus;
import me.erano.com.api.cluster.Backend;
import me.erano.com.api.cluster.Cluster;
import me.erano.com.api.cluster.ClusterException;
import me.erano.com.api.cluster.ClusterSettings;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

/**
 * The network's state in Redis, under the prefix:
 * <ul>
 * <li>{@code servers}: sorted set of server names, scored with when they last wrote (Redis' clock, milliseconds);</li>
 * <li>{@code server:<server>}: hash of that server's arenas ({@link ArenaCodec}), expiring
 * {@link Cluster#FRESH_SECONDS} after the last write, so a crashed server's arenas go on their own;</li>
 * <li>{@code join:<uuid>}: where a player is going ({@code server<TAB>arena}), expiring after
 * {@link Cluster#JOIN_SECONDS};</li>
 * <li>{@code seats:<server>:<arena>}: the players on their way there with a seat ({@link #reserve}), sorted set scored
 * with when the seat lapses;</li>
 * <li>{@code lock:<name>}: a lock's holder, expiring when not renewed ({@link #claim});</li>
 * <li>channel {@code arenas}: a server's name, published whenever its arenas change.</li>
 * </ul>
 * A server's arenas are replaced in one transaction, so readers never see half of them.
 */
public final class RedisBackend implements Backend {

    private static final int TIMEOUT_MILLIS = (int) TimeUnit.SECONDS.toMillis(5);
    private static final long FORGET_MILLIS = TimeUnit.DAYS.toMillis(1);
    private static final long RESUBSCRIBE_MILLIS = TimeUnit.SECONDS.toMillis(5);

    private final JedisPool pool;
    private final HostAndPort address;
    private final JedisClientConfig client;
    private final Logger logger;
    private final String servers;
    private final String serverPrefix;
    private final String joinPrefix;
    private final String channel;
    private final String seatPrefix;
    private final String lockPrefix;
    private volatile boolean closed;
    private volatile JedisPubSub subscription;
    private Thread subscriber;

    private RedisBackend(JedisPool pool, HostAndPort address, JedisClientConfig client, String prefix, Logger logger) {
        this.pool = pool;
        this.address = address;
        this.client = client;
        this.logger = logger;
        this.servers = prefix + "servers";
        this.serverPrefix = prefix + "server:";
        this.joinPrefix = prefix + "join:";
        this.channel = prefix + "arenas";
        this.seatPrefix = prefix + "seats:";
        this.lockPrefix = prefix + "lock:";
    }

    public static RedisBackend connect(ClusterSettings settings, Logger logger) throws ClusterException {
        HostAndPort address = new HostAndPort(settings.redisHost(), settings.redisPort());
        DefaultJedisClientConfig.Builder client = DefaultJedisClientConfig.builder()
                .timeoutMillis(TIMEOUT_MILLIS)
                .database(settings.redisDatabase())
                .ssl(settings.redisSsl())
                .clientName("EranoAPI-Cluster" + (settings.serverName().isEmpty() ? ""
                        : "-" + settings.serverName().replaceAll("[^A-Za-z0-9_-]", "_")));
        if (!settings.redisUser().isEmpty()) {
            client.user(settings.redisUser());
        }
        if (!settings.redisPassword().isEmpty()) {
            client.password(settings.redisPassword());
        }
        JedisClientConfig config = client.build();
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // Calls run one after another; the subscription has a connection of its own.
        poolConfig.setMaxTotal(2);
        poolConfig.setMaxIdle(2);
        poolConfig.setMinIdle(1);
        JedisPool pool = new JedisPool(poolConfig, address, config);
        try (Jedis jedis = pool.getResource()) {
            jedis.ping();
        } catch (RuntimeException e) {
            pool.close();
            throw new ClusterException(settings.describe() + " can't be reached: " + e.getMessage(), e);
        }
        return new RedisBackend(pool, address, config, settings.prefix(), logger);
    }

    private String serverKey(String server) {
        return serverPrefix + server;
    }

    private String joinKey(UUID player) {
        return joinPrefix + player;
    }

    /** Redis' clock, so servers with wrong clocks don't matter. */
    private static long now(Jedis jedis) {
        List<String> time = jedis.time();
        return Long.parseLong(time.get(0)) * 1000L + Long.parseLong(time.get(1)) / 1000L;
    }

    @Override
    public void publish(String server, Collection<ArenaStatus> arenas) {
        Map<String, String> values = new LinkedHashMap<>();
        for (ArenaStatus arena : arenas) {
            values.put(arena.arena(), ArenaCodec.encode(arena));
        }
        String key = serverKey(server);
        try (Jedis jedis = pool.getResource()) {
            long now = now(jedis);
            Transaction transaction = jedis.multi();
            transaction.del(key);
            if (!values.isEmpty()) {
                transaction.hset(key, values);
                transaction.expire(key, Cluster.FRESH_SECONDS);
            }
            transaction.zadd(servers, now, server);
            transaction.publish(channel, server);
            transaction.exec();
        }
    }

    @Override
    public void remove(String server) {
        try (Jedis jedis = pool.getResource()) {
            Transaction transaction = jedis.multi();
            transaction.del(serverKey(server));
            transaction.zrem(servers, server);
            transaction.publish(channel, server);
            transaction.exec();
        }
    }

    @Override
    public List<ArenaStatus> arenas() {
        List<ArenaStatus> list = new ArrayList<>();
        try (Jedis jedis = pool.getResource()) {
            long now = now(jedis);
            List<String> names = jedis.zrangeByScore(servers, now - Cluster.FRESH_SECONDS * 1000L,
                    Double.POSITIVE_INFINITY);
            Pipeline pipeline = jedis.pipelined();
            Map<String, Response<Map<String, String>>> responses = new LinkedHashMap<>();
            for (String server : names) {
                responses.put(server, pipeline.hgetAll(serverKey(server)));
            }
            pipeline.sync();
            for (Map.Entry<String, Response<Map<String, String>>> entry : responses.entrySet()) {
                for (Map.Entry<String, String> arena : entry.getValue().get().entrySet()) {
                    ArenaStatus status = ArenaCodec.decode(entry.getKey(), arena.getKey(), arena.getValue());
                    if (status != null) {
                        list.add(status);
                    }
                }
            }
        }
        Collections.sort(list, new Comparator<ArenaStatus>() {
            @Override
            public int compare(ArenaStatus a, ArenaStatus b) {
                int server = a.server().compareTo(b.server());
                return server != 0 ? server : a.arena().compareTo(b.arena());
            }
        });
        return list;
    }

    @Override
    public boolean isWritten(String server) {
        try (Jedis jedis = pool.getResource()) {
            Double written = jedis.zscore(servers, server);
            return written != null && written >= now(jedis) - Cluster.FRESH_SECONDS * 1000L;
        }
    }

    @Override
    public void forgetGone() {
        try (Jedis jedis = pool.getResource()) {
            jedis.zremrangeByScore(servers, Double.NEGATIVE_INFINITY, now(jedis) - FORGET_MILLIS);
        }
    }

    @Override
    public void sendTo(UUID player, String server, String arena) {
        try (Jedis jedis = pool.getResource()) {
            jedis.setex(joinKey(player), Cluster.JOIN_SECONDS, ArenaCodec.encodeJoin(server, arena));
        }
    }

    /**
     * One script, so Redis runs it as one step: drop lapsed seats, read the arena as its server wrote it, count its
     * players and seats, then take a seat and write the join (freeing a seat the player held elsewhere).
     * KEYS: server hash, seats of the arena, the player's join. ARGV: arena, player, now (ms), join seconds, join
     * value, seat prefix.
     */
    private static final String RESERVE = ""
            + "redis.call('ZREMRANGEBYSCORE', KEYS[2], '-inf', ARGV[3])\n"
            + "local value = redis.call('HGET', KEYS[1], ARGV[1])\n"
            + "if not value then return 0 end\n"
            + "local f = {}\n"
            + "local from = 1\n"
            + "while true do\n"
            + "  local tab = string.find(value, '\\t', from, true)\n"
            + "  if not tab then f[#f + 1] = string.sub(value, from) break end\n"
            + "  f[#f + 1] = string.sub(value, from, tab - 1)\n"
            + "  from = tab + 1\n"
            + "end\n"
            // format, game, state, stage, paused, seconds, players, spectators, max, joinable (ArenaCodec)
            + "if f[1] ~= '1' or f[4] ~= 'WAITING' or f[10] ~= '1' then return 0 end\n"
            + "local players = tonumber(f[7])\n"
            + "local max = tonumber(f[9])\n"
            + "local held = redis.call('ZSCORE', KEYS[2], ARGV[2])\n"
            + "if not held and max > 0 and players + redis.call('ZCARD', KEYS[2]) >= max then return 0 end\n"
            + "local old = redis.call('GET', KEYS[3])\n"
            + "if old then\n"
            + "  local tab = string.find(old, '\\t', 1, true)\n"
            + "  if tab then\n"
            + "    redis.call('ZREM', ARGV[6] .. string.sub(old, 1, tab - 1) .. ':' .. string.sub(old, tab + 1), ARGV[2])\n"
            + "  end\n"
            + "end\n"
            + "local lapses = tonumber(ARGV[3]) + tonumber(ARGV[4]) * 1000\n"
            + "redis.call('ZADD', KEYS[2], lapses, ARGV[2])\n"
            + "redis.call('PEXPIRE', KEYS[2], tonumber(ARGV[4]) * 1000)\n"
            + "redis.call('SET', KEYS[3], ARGV[5], 'EX', tonumber(ARGV[4]))\n"
            + "return 1\n";

    private String seatKey(String server, String arena) {
        return seatPrefix + server + ":" + arena;
    }

    @Override
    public boolean reserve(UUID player, String server, String arena) {
        try (Jedis jedis = pool.getResource()) {
            long now = now(jedis);
            Object reserved = jedis.eval(RESERVE,
                    Arrays.asList(serverKey(server), seatKey(server, arena), joinKey(player)),
                    Arrays.asList(arena, player.toString(), String.valueOf(now), String.valueOf(Cluster.JOIN_SECONDS),
                            ArenaCodec.encodeJoin(server, arena), seatPrefix));
            return Long.valueOf(1).equals(reserved);
        }
    }

    @Override
    public String pending(UUID player, String server) {
        try (Jedis jedis = pool.getResource()) {
            return ArenaCodec.joinArena(jedis.get(joinKey(player)), server);
        }
    }

    @Override
    public String arriving(UUID player, String server) {
        try (Jedis jedis = pool.getResource()) {
            // GET and DEL in one go (GETDEL needs Redis 6.2).
            Transaction transaction = jedis.multi();
            Response<String> value = transaction.get(joinKey(player));
            transaction.del(joinKey(player));
            transaction.exec();
            String arena = ArenaCodec.joinArena(value.get(), server);
            if (arena != null) {
                // Arrived: the seat is the player now, counted in the arena's players.
                jedis.zrem(seatKey(server, arena), player.toString());
            }
            return arena;
        }
    }

    /** KEYS: the lock. ARGV: owner, seconds. Takes or renews it; returns who holds it. */
    private static final String CLAIM = ""
            + "local holder = redis.call('GET', KEYS[1])\n"
            + "if holder and holder ~= ARGV[1] then return holder end\n"
            + "redis.call('SET', KEYS[1], ARGV[1], 'EX', tonumber(ARGV[2]))\n"
            + "return ARGV[1]\n";

    /** KEYS: the lock. ARGV: owner. Deletes it only if owner holds it. */
    private static final String RELEASE = ""
            + "if redis.call('GET', KEYS[1]) == ARGV[1] then redis.call('DEL', KEYS[1]) end\n"
            + "return 0\n";

    @Override
    public String claim(String name, String owner, int seconds) {
        try (Jedis jedis = pool.getResource()) {
            return String.valueOf(jedis.eval(CLAIM, Collections.singletonList(lockPrefix + name),
                    Arrays.asList(owner, String.valueOf(seconds))));
        }
    }

    @Override
    public void release(String name, String owner) {
        try (Jedis jedis = pool.getResource()) {
            jedis.eval(RELEASE, Collections.singletonList(lockPrefix + name), Collections.singletonList(owner));
        }
    }

    @Override
    public boolean listen(final ChangeListener changed) {
        subscriber = new Thread(() -> {
            boolean failing = false;
            while (!closed) {
                try (Jedis jedis = new Jedis(address, client)) {
                    JedisPubSub pubSub = new JedisPubSub() {
                        @Override
                        public void onMessage(String channel, String server) {
                            changed.changed(server);
                        }
                    };
                    subscription = pubSub;
                    if (failing) {
                        logger.info("Cluster: listening to Redis again");
                        failing = false;
                    }
                    jedis.subscribe(pubSub, channel);
                } catch (RuntimeException e) {
                    if (closed) {
                        return;
                    }
                    if (!failing) {
                        // Logged once per outage; readers still read, they just aren't told at once meanwhile.
                        logger.log(Level.WARNING, "Cluster: lost the Redis subscription (" + e.getMessage()
                                + "); trying again every " + RESUBSCRIBE_MILLIS / 1000 + " seconds");
                        failing = true;
                    }
                }
                try {
                    Thread.sleep(RESUBSCRIBE_MILLIS);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }, "EranoAPI-Cluster Redis subscription");
        subscriber.setDaemon(true);
        subscriber.start();
        return true;
    }

    @Override
    public void close() {
        closed = true;
        JedisPubSub pubSub = subscription;
        if (pubSub != null && pubSub.isSubscribed()) {
            try {
                pubSub.unsubscribe();
            } catch (RuntimeException ignored) {
                // The connection goes anyway.
            }
        }
        if (subscriber != null) {
            subscriber.interrupt();
        }
        pool.close();
    }
}
