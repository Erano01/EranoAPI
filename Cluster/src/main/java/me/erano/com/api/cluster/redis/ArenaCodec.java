package me.erano.com.api.cluster.redis;

import me.erano.com.api.cluster.ArenaStatus;

/**
 * An arena as one Redis hash value: {@code 1} (the format), then game, state, stage, paused, seconds left, players,
 * spectators, max players and joinable, separated by tabs. Names have no control characters ({@link ArenaStatus}),
 * so no tab can be in one.
 */
final class ArenaCodec {

    private static final String FORMAT = "1";
    private static final char SEPARATOR = '\t';

    private ArenaCodec() {
    }

    static String encode(ArenaStatus arena) {
        return FORMAT + SEPARATOR + arena.game() + SEPARATOR + arena.state() + SEPARATOR + arena.stage().name()
                + SEPARATOR + (arena.paused() ? 1 : 0) + SEPARATOR + arena.secondsLeft() + SEPARATOR + arena.players()
                + SEPARATOR + arena.spectators() + SEPARATOR + arena.maxPlayers() + SEPARATOR
                + (arena.joinable() ? 1 : 0);
    }

    /** {@code null} for a value this version can't read (a newer format, or broken). */
    static ArenaStatus decode(String server, String arena, String value) {
        String[] fields = value.split(String.valueOf(SEPARATOR), -1);
        if (fields.length != 10 || !FORMAT.equals(fields[0])) {
            return null;
        }
        try {
            ArenaStatus.Stage stage;
            try {
                stage = ArenaStatus.Stage.valueOf(fields[3]);
            } catch (IllegalArgumentException e) {
                stage = ArenaStatus.Stage.CLOSED;
            }
            return new ArenaStatus(fields[1], server, arena, fields[2], stage, "1".equals(fields[4]),
                    Integer.parseInt(fields[5]), Integer.parseInt(fields[6]), Integer.parseInt(fields[7]),
                    Integer.parseInt(fields[8]), "1".equals(fields[9]));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /** A pending join: server and arena. */
    static String encodeJoin(String server, String arena) {
        return server + SEPARATOR + arena;
    }

    /** The arena if the join is for {@code server}, else {@code null}. */
    static String joinArena(String value, String server) {
        if (value == null) {
            return null;
        }
        int separator = value.indexOf(SEPARATOR);
        if (separator < 0 || !value.substring(0, separator).equals(server)) {
            return null;
        }
        return value.substring(separator + 1);
    }
}
