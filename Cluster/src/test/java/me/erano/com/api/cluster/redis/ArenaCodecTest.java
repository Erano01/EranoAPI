package me.erano.com.api.cluster.redis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import me.erano.com.api.cluster.ArenaStatus;

class ArenaCodecTest {

    @Test
    void anArenaComesBackAsItWent() {
        ArenaStatus arena = new ArenaStatus("hungergames", "hg-1", "breeze", "BATTLE", ArenaStatus.Stage.PLAYING, true,
                95, 12, 3, 24, false);
        assertEquals(arena, ArenaCodec.decode("hg-1", "breeze", ArenaCodec.encode(arena)));
    }

    @Test
    void unknownFormatsAndStagesDontBreakReading() {
        assertNull(ArenaCodec.decode("hg-1", "breeze", "2\tsomething new"));
        assertNull(ArenaCodec.decode("hg-1", "breeze", "1\thungergames\tBATTLE"));
        ArenaStatus later = ArenaCodec.decode("hg-1", "breeze", "1\thungergames\tX\tQUEUED\t0\t0\t0\t0\t0\t0");
        assertEquals(ArenaStatus.Stage.CLOSED, later.stage());
    }

    @Test
    void aJoinIsOnlyForItsServer() {
        String join = ArenaCodec.encodeJoin("hg-2", "sg4");
        assertEquals("sg4", ArenaCodec.joinArena(join, "hg-2"));
        assertNull(ArenaCodec.joinArena(join, "hg-1"));
        assertNull(ArenaCodec.joinArena(null, "hg-1"));
    }
}
