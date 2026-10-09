package me.erano.com.api.cluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.erano.com.api.cluster.mysql.MySqlBackend;
import me.erano.com.api.cluster.redis.RedisBackend;

/**
 * What the servers of a minigame network know about each other: every server's arenas, and the players on their way
 * to one. Game servers publish their arenas, hubs and proxies read them, anyone sends a player to an arena; the
 * server the player arrives at takes the arena and puts them in it.
 * <p>
 * Calls run one after another on a thread of their own, never on the caller's: the futures complete there, so a
 * Bukkit plugin goes back to its main thread before touching the game. A failed call's future fails with the reason.
 */
public final class Cluster implements AutoCloseable {

    /** Arenas written longer ago are of a server that's gone (crashed): they aren't read. */
    public static final int FRESH_SECONDS = 15;
    /** A player not arrived after this long isn't coming: the arena they were sent to is ignored. */
    public static final int JOIN_SECONDS = 60;

    private static final long CLOSE_WAIT_SECONDS = 10;

    private final ClusterSettings settings;
    private final Backend backend;
    private final Logger logger;
    private final ExecutorService thread;
    private final List<Consumer<String>> listeners = new CopyOnWriteArrayList<>();
    private final boolean pushes;

    Cluster(ClusterSettings settings, Backend backend, Logger logger) {
        this.settings = settings;
        this.backend = backend;
        this.logger = logger;
        this.thread = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable, "EranoAPI-Cluster");
            thread.setDaemon(true);
            return thread;
        });
        this.pushes = backend.listen(server -> {
            for (Consumer<String> listener : listeners) {
                try {
                    listener.accept(server);
                } catch (RuntimeException e) {
                    logger.log(Level.WARNING, "Cluster: a change listener failed", e);
                }
            }
        });
    }

    /**
     * Connects (a few seconds at most) and makes what the backend needs (tables) when it's missing.
     *
     * @throws ClusterException when it can't be reached
     */
    public static Cluster connect(ClusterSettings settings, Logger logger) throws ClusterException {
        Backend backend;
        switch (settings.backend()) {
            case REDIS:
                backend = RedisBackend.connect(settings, logger);
                break;
            case MYSQL:
            default:
                backend = MySqlBackend.connect(settings, logger);
                break;
        }
        return new Cluster(settings, backend, logger);
    }

    /** For tests and other backends. */
    public static Cluster of(ClusterSettings settings, Backend backend, Logger logger) {
        return new Cluster(settings, backend, logger);
    }

    public ClusterSettings settings() {
        return settings;
    }

    /**
     * Whether changes are pushed to {@link #onChange} listeners as they happen (Redis); without it (MySQL) readers ask
     * every few seconds.
     */
    public boolean pushesChanges() {
        return pushes;
    }

    /**
     * {@code listener} gets a server's name whenever its arenas change, on a thread of the backend's own; only when
     * {@link #pushesChanges()}.
     */
    public void onChange(Consumer<String> listener) {
        listeners.add(listener);
    }

    /** Replaces this server's arenas with {@code arenas} (all of them, also the unchanged ones). */
    public CompletableFuture<Void> publish(final String server, Collection<ArenaStatus> arenas) {
        final List<ArenaStatus> copy = new ArrayList<>(arenas);
        for (ArenaStatus arena : copy) {
            if (!arena.server().equals(server)) {
                throw new IllegalArgumentException(arena + " isn't an arena of " + server);
            }
        }
        return run(() -> {
            backend.publish(server, copy);
            return null;
        });
    }

    /** This server's arenas are gone: it stops. */
    public CompletableFuture<Void> remove(final String server) {
        return run(() -> {
            backend.remove(server);
            return null;
        });
    }

    /** The arenas of every server that wrote them lately, by server and arena name. */
    public CompletableFuture<List<ArenaStatus>> arenas() {
        return run(backend::arenas);
    }

    /** Whether some server is writing arenas as {@code server} now: two servers with one name. */
    public CompletableFuture<Boolean> isWritten(final String server) {
        return run(() -> backend.isWritten(server));
    }

    /** Forgets servers gone for a day. */
    public CompletableFuture<Void> forgetGone() {
        return run(() -> {
            backend.forgetGone();
            return null;
        });
    }

    /**
     * The player is on their way to {@code arena} of {@code server}: write this before sending them there (proxy
     * {@code Connect}), so it's there when they arrive.
     */
    public CompletableFuture<Void> sendTo(final UUID player, final String server, final String arena) {
        return run(() -> {
            backend.sendTo(player, server, arena);
            return null;
        });
    }

    /**
     * Sends the player to {@code arena} of {@code server} with a seat to play, if one is free: checked and written in
     * one atomic step, so two hubs (or menus) can't send two players to the last seat. Completes with {@code false},
     * having written nothing, when the arena is full counting the players on their way; quick join then tries the
     * next arena. To watch a running game use {@link #sendTo}.
     */
    public CompletableFuture<Boolean> reserve(final UUID player, final String server, final String arena) {
        return run(() -> backend.reserve(player, server, arena));
    }

    /**
     * Takes the network-wide lock {@code name} (e.g. {@code map:breeze} while a map is edited) for {@code owner} (a
     * server name) for {@code seconds}, or renews it. Renew it before it lapses; a holder that crashed loses it after
     * {@code seconds}. Completes with who holds it now: {@code owner} if taken, else the other holder.
     */
    public CompletableFuture<String> claim(final String name, final String owner, final int seconds) {
        if (seconds < 1) {
            throw new IllegalArgumentException("seconds must be 1 or more: " + seconds);
        }
        return run(() -> backend.claim(name, owner, seconds));
    }

    /** Gives the lock {@code name} up, if {@code owner} holds it. */
    public CompletableFuture<Void> release(final String name, final String owner) {
        return run(() -> {
            backend.release(name, owner);
            return null;
        });
    }

    /** Where the player is going on {@code server}, {@code null} if nowhere; unlike {@link #arriving}, left in place. */
    public CompletableFuture<String> pending(final UUID player, final String server) {
        return run(() -> backend.pending(player, server));
    }

    /**
     * The arena the player arriving at {@code server} was sent to, {@code null} if none; ask when they join. Used
     * once, and frees the player's seat.
     */
    public CompletableFuture<String> arriving(final UUID player, final String server) {
        return run(() -> backend.arriving(player, server));
    }

    /** Where it is, without passwords: for logs. */
    public String describe() {
        return settings.describe();
    }

    /** Waits for the queued calls (a few seconds at most), then disconnects. */
    @Override
    public void close() {
        thread.shutdown();
        try {
            if (!thread.awaitTermination(CLOSE_WAIT_SECONDS, TimeUnit.SECONDS)) {
                logger.warning("Cluster: calls still waiting after " + CLOSE_WAIT_SECONDS + " seconds; dropped");
                thread.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        backend.close();
    }

    private interface Call<T> {
        T run() throws Exception;
    }

    private <T> CompletableFuture<T> run(final Call<T> call) {
        final CompletableFuture<T> result = new CompletableFuture<>();
        try {
            thread.execute(() -> {
                try {
                    result.complete(call.run());
                } catch (Exception e) {
                    result.completeExceptionally(e instanceof ClusterException ? e
                            : new ClusterException(e.getMessage() == null ? e.toString() : e.getMessage(), e));
                }
            });
        } catch (RejectedExecutionException e) {
            result.completeExceptionally(new ClusterException("the network connection is closed", e));
        }
        return result;
    }
}
