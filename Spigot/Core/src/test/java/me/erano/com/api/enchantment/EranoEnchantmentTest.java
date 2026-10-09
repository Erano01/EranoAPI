package me.erano.com.api.enchantment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import me.erano.com.api.internal.ServerNames;

class EranoEnchantmentTest {

    private static ServerNames<EranoEnchantment, String> server(String... fields) {
        Set<String> names = new HashSet<>(Arrays.asList(fields));
        return new ServerNames<>(EranoEnchantment.class, EranoEnchantment.table(), name -> names.contains(name) ? name : null);
    }

    @Test
    void oldServersUseTheirOwnName() {
        ServerNames<EranoEnchantment, String> v1_8 = server("DAMAGE_ALL", "DURABILITY", "ARROW_INFINITE");
        assertEquals("DAMAGE_ALL", v1_8.get(EranoEnchantment.SHARPNESS));
        assertEquals("DURABILITY", v1_8.get(EranoEnchantment.UNBREAKING));
        assertEquals("ARROW_INFINITE", v1_8.get(EranoEnchantment.INFINITY));
        assertNull(v1_8.get(EranoEnchantment.MENDING));
        assertEquals(16, EranoEnchantment.SHARPNESS.legacyId());
        assertEquals(-1, EranoEnchantment.DENSITY.legacyId());
    }

    @Test
    void matchesNamesOldNamesAndKeys() {
        assertEquals(EranoEnchantment.SHARPNESS, EranoEnchantment.match("DAMAGE_ALL").get());
        assertEquals(EranoEnchantment.SHARPNESS, EranoEnchantment.match("minecraft:sharpness").get());
        assertEquals(EranoEnchantment.LUCK_OF_THE_SEA, EranoEnchantment.match("luck").get());
        assertEquals(EranoEnchantment.SWEEPING_EDGE, EranoEnchantment.match("sweeping_edge").get());
    }
}
