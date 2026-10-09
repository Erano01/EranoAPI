package me.erano.com.api.cluster;

import java.util.Collection;

/**
 * Picks the arena to play in now: the fullest one that can still be joined, so it starts soonest; between equally
 * full ones, the one with the fewest seconds left. The same rule for hubs, proxies and game servers.
 */
public final class QuickJoin {

    private String game;
    private String server;
    private String arena;
    private String exceptServer;
    private String exceptArena;

    private QuickJoin() {
    }

    public static QuickJoin any() {
        return new QuickJoin();
    }

    /** Only arenas of this minigame. */
    public QuickJoin game(String game) {
        this.game = game;
        return this;
    }

    /** Only arenas of this server. */
    public QuickJoin server(String server) {
        this.server = server;
        return this;
    }

    /** Only arenas of this name (a map), on any server; case doesn't matter. */
    public QuickJoin arena(String arena) {
        this.arena = arena;
        return this;
    }

    /** Not this arena (the one the player waits in already). */
    public QuickJoin except(String server, String arena) {
        this.exceptServer = server;
        this.exceptArena = arena;
        return this;
    }

    /** The best of {@code all}; {@code null} if none can be joined. */
    public ArenaStatus best(Collection<ArenaStatus> all) {
        ArenaStatus best = null;
        for (ArenaStatus candidate : all) {
            if (!candidate.joinable()
                    || game != null && !candidate.game().equals(game)
                    || server != null && !candidate.server().equals(server)
                    || arena != null && !candidate.arena().equalsIgnoreCase(arena)
                    || candidate.server().equals(exceptServer) && candidate.arena().equals(exceptArena)) {
                continue;
            }
            if (best == null || candidate.players() > best.players()
                    || candidate.players() == best.players() && candidate.secondsLeft() < best.secondsLeft()) {
                best = candidate;
            }
        }
        return best;
    }
}
