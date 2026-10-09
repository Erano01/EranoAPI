package me.erano.com.api.particle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import me.erano.com.api.internal.ServerNames;

class EranoParticleTest {

    @Test
    void particlesOn1_8And1_20_4() {
        // 1.8's EnumParticle and 1.9 - 1.20.4's Particle share their names
        Set<String> v1_8 = new HashSet<>(Arrays.asList("REDSTONE", "VILLAGER_HAPPY", "BLOCK_CRACK", "BLOCK_DUST",
                "SPELL_MOB", "EXPLOSION_NORMAL"));
        ServerNames<EranoParticle, String> server = new ServerNames<>(EranoParticle.class, EranoParticle.table(),
                name -> v1_8.contains(name) ? name : null);
        assertEquals("REDSTONE", server.get(EranoParticle.DUST));
        assertEquals("VILLAGER_HAPPY", server.get(EranoParticle.HAPPY_VILLAGER));
        assertEquals("BLOCK_CRACK", server.get(EranoParticle.BLOCK));
        assertEquals("SPELL_MOB", server.get(EranoParticle.ENTITY_EFFECT));
        assertEquals("EXPLOSION_NORMAL", server.get(EranoParticle.POOF));
        assertNull(server.get(EranoParticle.SONIC_BOOM));
    }

    @Test
    void matchesAndEffects() {
        assertEquals(EranoParticle.DUST, EranoParticle.match("redstone").get());
        assertEquals(EranoParticle.HAPPY_VILLAGER, EranoParticle.match("minecraft:happy_villager").get());
        assertEquals("V1_8_R1", EranoEffect.STEP_SOUND.since());
        assertEquals(EranoEffect.STEP_SOUND, EranoEffect.match("step_sound").get());
    }
}
