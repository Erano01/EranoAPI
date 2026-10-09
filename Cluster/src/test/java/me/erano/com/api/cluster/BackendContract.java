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
    }
}
