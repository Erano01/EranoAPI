package me.erano.com.common;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class VersionRangeTest {

    private static boolean contains(VersionRange range, String version) {
        return range.contains(MinecraftVersion.parse(version));
    }

    @Test
    void betweenIsInclusiveOnBothEnds() {
        VersionRange r3 = VersionRange.between("1.8.4", "1.8.9");
        assertFalse(contains(r3, "1.8.3"));
        assertTrue(contains(r3, "1.8.4"));
        assertTrue(contains(r3, "1.8.9"));
        assertFalse(contains(r3, "1.8.10"));
        assertFalse(contains(r3, "1.9"));
    }

    @Test
    void betweenStartingAtAReleaseIncludesIt() {
        VersionRange r1 = VersionRange.between("1.21", "1.21.1");
        assertTrue(contains(r1, "1.21"));
        assertTrue(contains(r1, "1.21.1"));
        assertFalse(contains(r1, "1.21.2"));
    }

    @Test
    void onlyMatchesOneRelease() {
        VersionRange range = VersionRange.only("1.21.4");
        assertFalse(contains(range, "1.21.3"));
        assertTrue(contains(range, "1.21.4"));
        assertFalse(contains(range, "1.21.5"));
    }

    @Test
    void seriesCoversEveryPatch() {
        VersionRange range = VersionRange.series("26.1");
        assertTrue(contains(range, "26.1"));
        assertTrue(contains(range, "26.1.2"));
        assertTrue(contains(range, "26.1.99"));
        assertFalse(contains(range, "26.2"));
        assertFalse(contains(range, "1.21.11"));
    }

    @Test
    void atLeastHasNoUpperBound() {
        VersionRange range = VersionRange.atLeast("26.3");
        assertFalse(contains(range, "26.2"));
        assertTrue(contains(range, "26.3"));
        assertTrue(contains(range, "27.4.1"));
    }

    @Test
    void rejectsEmptyRanges() {
        assertThrows(IllegalArgumentException.class, () -> VersionRange.between("1.9", "1.8"));
    }
}
