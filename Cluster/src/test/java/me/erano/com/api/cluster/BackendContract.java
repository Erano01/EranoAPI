package me.erano.com.api.cluster;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/** What every backend must do; each backend's test runs it. */
public final class BackendContract {

    private BackendContract() {
    }

    public static void check(Backend backend) throws Exception {
        ArenaStatus breeze = QuickJoinTest.arena("hg-1", "breeze", 3, 20, true);
        ArenaStatus sg4 = new ArenaStatus("hungergames", "hg-1", "sg4", "BATTLE", ArenaStatus.Stage.PLAYING, true,
                90, 7, 2, 0, false);
        ArenaStatus other = QuickJoinTest.arena("hg-2", "breeze", 0, 30, true);
        backend.publish("hg-1", Arrays.asList(breeze, sg4));
        backend.publish("hg-2", Collections.singletonList(other));
        assertEquals(Arrays.asList(breeze, sg4, other), backend.arenas());
        assertTrue(backend.isWritten("hg-1"));
        assertFalse(backend.isWritten("hg-3"));

        // Published again without sg4: it's gone; breeze changed.
        ArenaStatus fuller = QuickJoinTest.arena("hg-1", "breeze", 4, 19, true);
        backend.publish("hg-1", Collections.singletonList(fuller));
        assertEquals(Arrays.asList(fuller, other), backend.arenas());

        backend.remove("hg-2");
        List<ArenaStatus> left = backend.arenas();
        assertEquals(Collections.singletonList(fuller), left);
        backend.forgetGone();
        assertEquals(left, backend.arenas());

        UUID player = UUID.randomUUID();
        backend.sendTo(player, "hg-2", "sg4");
        backend.sendTo(player, "hg-1", "breeze");
        // The newer one counts; another server doesn't get it, and it's gone after one read either way.
        assertNull(backend.arriving(player, "hg-2"));
        assertNull(backend.arriving(player, "hg-1"));
        backend.sendTo(player, "hg-1", "breeze");
        assertEquals("breeze", backend.arriving(player, "hg-1"));
        assertNull(backend.arriving(player, "hg-1"));
        backend.remove("hg-1");
        assertEquals(Collections.emptyList(), backend.arenas());

        checkSeats(backend);
        checkLocks(backend);
    }

    /** {@link Backend#claim}: one holder at a time, renewable, released only by its holder, lapsing in time. */
    private static void checkLocks(Backend backend) throws Exception {
        assertEquals("hg-1", backend.claim("map:breeze", "hg-1", 30));
        assertEquals("hg-1", backend.claim("map:breeze", "hg-2", 30), "held by another");
        assertEquals("hg-1", backend.claim("map:breeze", "hg-1", 30), "renewed by its holder");
        backend.release("map:breeze", "hg-2");
        assertEquals("hg-1", backend.claim("map:breeze", "hg-2", 30), "only its holder releases it");
        backend.release("map:breeze", "hg-1");
        assertEquals("hg-2", backend.claim("map:breeze", "hg-2", 1));
        Thread.sleep(2100);
        assertEquals("hg-1", backend.claim("map:breeze", "hg-1", 30), "a lock not renewed lapses");
        backend.release("map:breeze", "hg-1");
    }

    /** {@link Backend#reserve}: the last seat goes once, a player keeps their own, arriving frees it. */
    private static void checkSeats(Backend backend) throws Exception {
        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();
        // 3 of 4 playing: one seat left.
        ArenaStatus almostFull = new ArenaStatus("hungergames", "hg-5", "breeze", "STARTING_COUNTDOWN",
                ArenaStatus.Stage.WAITING, false, 20, 3, 0, 4, true);
        ArenaStatus running = new ArenaStatus("hungergames", "hg-5", "sg4", "BATTLE", ArenaStatus.Stage.PLAYING,
                false, 90, 4, 0, 4, false);
        backend.publish("hg-5", Arrays.asList(almostFull, running));

        assertTrue(backend.reserve(first, "hg-5", "breeze"));
        assertFalse(backend.reserve(second, "hg-5", "breeze"), "the last seat is taken");
        assertTrue(backend.reserve(first, "hg-5", "breeze"), "asking again keeps the own seat");
        assertEquals("breeze", backend.pending(first, "hg-5"));
        assertEquals("breeze", backend.pending(first, "hg-5"), "pending leaves the join in place");
        assertNull(backend.pending(second, "hg-5"));

        // Arrived: counted as a player from the next publish on; the seat is free.
        assertEquals("breeze", backend.arriving(first, "hg-5"));
        assertTrue(backend.reserve(second, "hg-5", "breeze"));

        assertFalse(backend.reserve(first, "hg-5", "sg4"), "no seat in a running game");
        assertFalse(backend.reserve(first, "hg-5", "nowhere"));
        assertFalse(backend.reserve(first, "hg-6", "breeze"));

        // A seat taken elsewhere frees the old one.
        UUID third = UUID.randomUUID();
        ArenaStatus empty = new ArenaStatus("hungergames", "hg-5", "sg1", "STARTING_COUNTDOWN",
                ArenaStatus.Stage.WAITING, false, 20, 0, 0, 4, true);
        backend.publish("hg-5", Arrays.asList(almostFull, running, empty));
        assertTrue(backend.reserve(second, "hg-5", "sg1"));
        assertTrue(backend.reserve(third, "hg-5", "breeze"), "second's seat in breeze went with its new join");
        backend.remove("hg-5");
    }
}
