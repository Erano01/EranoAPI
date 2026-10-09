package me.erano.com.api.cluster;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Where the network's state is kept: {@link me.erano.com.api.cluster.mysql.MySqlBackend} or
 * {@link me.erano.com.api.cluster.redis.RedisBackend}. Every call blocks; {@link Cluster} runs them on a thread of
 * its own. Implementations are only called from that thread, except {@link #listen} and {@link #close}.
 */
public interface Backend {

    /** Replaces the server's arenas with {@code arenas}, marking them fresh; tells the listeners. */
    void publish(String server, Collection<ArenaStatus> arenas) throws Exception;

    /** The server's arenas are gone (it stops); tells the listeners. */
    void remove(String server) throws Exception;

    /** The arenas of every server written in the last {@link Cluster#FRESH_SECONDS}, by server and arena name. */
    List<ArenaStatus> arenas() throws Exception;

    /** Whether some server wrote arenas under {@code server} in the last {@link Cluster#FRESH_SECONDS}. */
    boolean isWritten(String server) throws Exception;

    /** Forgets servers gone for a day. */
    void forgetGone() throws Exception;

    /** The player is on their way to {@code arena} of {@code server}; replaces an earlier one. */
    void sendTo(UUID player, String server, String arena) throws Exception;

    /**
     * The arena the player is coming to on {@code server}, {@code null} if none, it's for another server or older than
     * {@link Cluster#JOIN_SECONDS}. Removed either way, so it's used once.
     */
    String arriving(UUID player, String server) throws Exception;

    /**
     * Runs {@code changed} with the server's name whenever a server's arenas change, on a thread of the backend's own.
     *
     * @return {@code false} when this backend can't tell (it's read every few seconds instead)
     */
    boolean listen(ChangeListener changed);

    /** Waits for nothing; the calls queued in {@link Cluster} are done by then. */
    void close();

    /** Told about changes; see {@link #listen}. */
    interface ChangeListener {
        void changed(String server);
    }
}
