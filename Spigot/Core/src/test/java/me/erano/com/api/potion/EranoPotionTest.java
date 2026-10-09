package me.erano.com.api.potion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import me.erano.com.api.internal.ServerNames;

class EranoPotionTest {

    @Test
    void effectsOnOldServers() {
        Set<String> v1_8 = new HashSet<>(Arrays.asList("INCREASE_DAMAGE", "JUMP", "SPEED", "HEAL"));
        ServerNames<EranoPotionEffect, String> server = new ServerNames<>(EranoPotionEffect.class,
                EranoPotionEffect.table(), name -> v1_8.contains(name) ? name : null);
        assertEquals("INCREASE_DAMAGE", server.get(EranoPotionEffect.STRENGTH));
        assertEquals("JUMP", server.get(EranoPotionEffect.JUMP_BOOST));
        assertEquals("SPEED", server.get(EranoPotionEffect.SPEED));
        assertEquals("HEAL", server.get(EranoPotionEffect.INSTANT_HEALTH));
        assertNull(server.get(EranoPotionEffect.LEVITATION));
        assertEquals(5, EranoPotionEffect.STRENGTH.id());
        assertEquals(EranoPotionEffect.STRENGTH, EranoPotionEffect.match("increase_damage").get());
    }

    @Test
    void typesOnOldServersAndTheirBase() {
        Set<String> v1_8 = new HashSet<>(Arrays.asList("SPEED", "INSTANT_HEAL", "JUMP", "WATER"));
        ServerNames<EranoPotionType, String> server = new ServerNames<>(EranoPotionType.class,
                EranoPotionType.table(), name -> v1_8.contains(name) ? name : null);
        assertEquals("SPEED", server.get(EranoPotionType.SWIFTNESS));
        assertEquals("INSTANT_HEAL", server.get(EranoPotionType.HEALING));
        assertEquals("JUMP", server.get(EranoPotionType.LEAPING));
        assertNull(server.get(EranoPotionType.LONG_SWIFTNESS));
        assertEquals(EranoPotionType.SWIFTNESS, EranoPotionType.LONG_SWIFTNESS.base());
        assertTrue(EranoPotionType.LONG_SWIFTNESS.isExtended());
        assertTrue(EranoPotionType.STRONG_HEALING.isUpgraded());
        assertFalse(EranoPotionType.SWIFTNESS.isExtended());
        assertEquals(EranoPotionType.SWIFTNESS, EranoPotionType.SWIFTNESS.base());
        assertEquals(EranoPotionType.HEALING, EranoPotionType.match("INSTANT_HEAL").get());
    }
}
