package me.erano.com.api.cluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * What a game server tells the network about one of its arenas. Written by that server, read by every other server,
 * hub and proxy. The same for every minigame: {@link #game()} says which one it is, {@link #stage()} what anyone can
 * do there, {@link #state()} the game's own words for where it is.
 */
public final class ArenaStatus {

    /** What a player can do in the arena, for any minigame. */
    public enum Stage {
        /** Before the game: players wait for it to start. Joinable unless it's full ({@link #joinable()}). */
        WAITING,
        /** The game runs: players can only watch. */
        PLAYING,
        /** Nobody can come in: being reset, edited, its world missing, no next game. */
        CLOSED
    }

    /** Longest {@link #game()}, {@link #server()}, {@link #arena()} and {@link #state()}, as stored. */
    public static final int MAX_NAME_LENGTH = 64;

    private final String game;
    private final String server;
    private final String arena;
    private final String state;
    private final Stage stage;
    private final boolean paused;
    private final int secondsLeft;
    private final int players;
    private final int spectators;
    private final int maxPlayers;
    private final boolean joinable;

    public ArenaStatus(String game, String server, String arena, String state, Stage stage, boolean paused,
                       int secondsLeft, int players, int spectators, int maxPlayers, boolean joinable) {
        this.game = name("game", game);
        this.server = name("server", server);
        this.arena = name("arena", arena);
        this.state = name("state", state);
        if (stage == null) {
            throw new IllegalArgumentException("stage is null");
        }
        this.stage = stage;
        this.paused = paused;
        this.secondsLeft = secondsLeft;
        this.players = players;
        this.spectators = spectators;
        this.maxPlayers = maxPlayers;
        this.joinable = joinable;
    }

    private static String name(String what, String value) {
        if (value == null || value.isEmpty() || value.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(what + " must have 1 - " + MAX_NAME_LENGTH + " characters: " + value);
        }
        for (int i = 0; i < value.length(); i++) {
            if (Character.isISOControl(value.charAt(i))) {
                throw new IllegalArgumentException(what + " has a control character: " + value);
            }
        }
        return value;
    }

    /** The minigame, e.g. {@code hungergames}. */
    public String game() {
        return game;
    }

    /** The server's name on the proxy. */
    public String server() {
        return server;
    }

    public String arena() {
        return arena;
    }

    /** The game's own state, e.g. a phase name; for menus, in the game's language files. */
    public String state() {
        return state;
    }

    public Stage stage() {
        return stage;
    }

    public boolean paused() {
        return paused;
    }

    /** Seconds left in the current state; 0 when it has no end. */
    public int secondsLeft() {
        return secondsLeft;
    }

    /** Waiting to play or alive. */
    public int players() {
        return players;
    }

    public int spectators() {
        return spectators;
    }

    /** 0 for no limit. */
    public int maxPlayers() {
        return maxPlayers;
    }

    /** A player could join to play now: it waits for the game and has room. */
    public boolean joinable() {
        return joinable;
    }

    /** Waiting for the game, but no room left: only to watch. */
    public boolean full() {
        return stage == Stage.WAITING && !joinable;
    }

    /** Each server's arenas, by server name, in the order given. */
    public static Map<String, List<ArenaStatus>> byServer(Collection<ArenaStatus> all) {
        Map<String, List<ArenaStatus>> servers = new LinkedHashMap<>();
        for (ArenaStatus arena : all) {
            List<ArenaStatus> arenas = servers.get(arena.server());
            if (arenas == null) {
                arenas = new ArrayList<>();
                servers.put(arena.server(), arenas);
            }
            arenas.add(arena);
        }
        return servers;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ArenaStatus)) {
            return false;
        }
        ArenaStatus that = (ArenaStatus) other;
        return game.equals(that.game) && server.equals(that.server) && arena.equals(that.arena)
                && state.equals(that.state) && stage == that.stage && paused == that.paused
                && secondsLeft == that.secondsLeft && players == that.players && spectators == that.spectators
                && maxPlayers == that.maxPlayers && joinable == that.joinable;
    }

    @Override
    public int hashCode() {
        return (server + '/' + arena).hashCode();
    }

    @Override
    public String toString() {
        return game + " " + server + "/" + arena + " " + state + " (" + stage + (paused ? ", paused" : "") + ", "
                + secondsLeft + "s, " + players + "/" + (maxPlayers == 0 ? "∞" : String.valueOf(maxPlayers))
                + ", " + spectators + " spectators" + (joinable ? ", joinable" : "") + ")";
    }
}
