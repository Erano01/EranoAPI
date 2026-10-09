package me.erano.com.api.cluster;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class QuickJoinTest {

    public static ArenaStatus arena(String server, String name, int players, int secondsLeft, boolean joinable) {
        return new ArenaStatus("hungergames", server, name, "STARTING_COUNTDOWN", ArenaStatus.Stage.WAITING, false,
                secondsLeft, players, 0, 24, joinable);
    }

    private final ArenaStatus fullest = arena("hg-2", "breeze", 9, 20, true);
    private final ArenaStatus emptier = arena("hg-1", "breeze", 3, 10, true);
    private final ArenaStatus closed = arena("hg-1", "sg4", 20, 5, false);
    private final List<ArenaStatus> all = Arrays.asList(emptier, closed, fullest);

    @Test
    void picksTheFullestArenaThatCanStillBeJoined() {
        assertEquals(fullest, QuickJoin.any().best(all));
        assertEquals(fullest, QuickJoin.any().arena("BREEZE").best(all));
        assertNull(QuickJoin.any().arena("sg4").best(all));
        assertNull(QuickJoin.any().arena("nowhere").best(all));
    }

    @Test
    void betweenEquallyFullOnesTheOneStartingSooner() {
        ArenaStatus sooner = arena("hg-1", "sg1", 9, 5, true);
        assertEquals(sooner, QuickJoin.any().best(Arrays.asList(fullest, sooner)));
    }

    @Test
    void narrowsToAServerAGameAndSkipsTheArenaThePlayerIsIn() {
        assertEquals(emptier, QuickJoin.any().server("hg-1").best(all));
        assertEquals(emptier, QuickJoin.any().except("hg-2", "breeze").best(all));
        assertNull(QuickJoin.any().game("skywars").best(all));
        assertEquals(fullest, QuickJoin.any().game("hungergames").best(all));
    }

    @Test
    void groupsByServerInOrder() {
        assertEquals(Arrays.asList("hg-1", "hg-2"),
                Arrays.asList(ArenaStatus.byServer(all).keySet().toArray(new String[0])));
        assertEquals(Arrays.asList(emptier, closed), ArenaStatus.byServer(all).get("hg-1"));
    }
}
