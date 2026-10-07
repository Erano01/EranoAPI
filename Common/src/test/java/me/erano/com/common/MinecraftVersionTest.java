package me.erano.com.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MinecraftVersionTest {

    @Test
    void parsesOldAndYearBasedVersions() {
        assertEquals(new MinecraftVersion(1, 8, 8), MinecraftVersion.parse("1.8.8"));
        assertEquals(new MinecraftVersion(1, 21, 0), MinecraftVersion.parse("1.21"));
        assertEquals(new MinecraftVersion(26, 1, 2), MinecraftVersion.parse("26.1.2"));
    }

    @Test
    void ignoresBukkitVersionSuffix() {
        assertEquals(new MinecraftVersion(1, 21, 11), MinecraftVersion.parse("1.21.11-R0.2-SNAPSHOT"));
        assertEquals(new MinecraftVersion(26, 1, 2), MinecraftVersion.parse("26.1.2-R0.1-SNAPSHOT"));
    }

    @Test
    void rejectsGarbage() {
        assertThrows(IllegalArgumentException.class, () -> MinecraftVersion.parse("latest"));
    }

    @Test
    void comparesNumericallyNotLexically() {
        assertTrue(MinecraftVersion.parse("1.10").compareTo(MinecraftVersion.parse("1.9.4")) > 0);
        assertTrue(MinecraftVersion.parse("1.21.10").compareTo(MinecraftVersion.parse("1.21.9")) > 0);
        assertTrue(MinecraftVersion.parse("26.1").compareTo(MinecraftVersion.parse("1.21.11")) > 0);
        assertEquals(0, MinecraftVersion.parse("1.21").compareTo(MinecraftVersion.parse("1.21.0")));
    }

    @Test
    void printsLikeMinecraft() {
        assertEquals("1.21", MinecraftVersion.parse("1.21.0").toString());
        assertEquals("26.1.2", MinecraftVersion.parse("26.1.2").toString());
    }
}
