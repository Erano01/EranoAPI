package me.erano.com.api.cluster.redis;

import java.util.ArrayList;
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

    @Override
    public String arriving(UUID player, String server) {
        try (Jedis jedis = pool.getResource()) {
            // GET and DEL in one go (GETDEL needs Redis 6.2).
            Transaction transaction = jedis.multi();
            Response<String> value = transaction.get(joinKey(player));
            transaction.del(joinKey(player));
            transaction.exec();
            return ArenaCodec.joinArena(value.get(), server);
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
