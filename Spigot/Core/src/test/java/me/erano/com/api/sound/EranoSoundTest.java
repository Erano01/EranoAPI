package me.erano.com.api.sound;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import me.erano.com.api.internal.ServerNames;

class EranoSoundTest {

    /** A server that has these Sound fields: the name it resolves to. */
    private static ServerNames<EranoSound, String> server(String... fields) {
        Set<String> names = new HashSet<>(Arrays.asList(fields));
        return new ServerNames<>(EranoSound.class, EranoSound.table(), name -> names.contains(name) ? name : null);
    }

    @Test
    void everySoundOfTodayIsAConstant() {
        assertEquals(1991, EranoSound.values().length);
        assertEquals("entity.player.levelup", EranoSound.ENTITY_PLAYER_LEVELUP.key());
    }

    @Test
    void oldServersPlayTheirOwnName() {
        ServerNames<EranoSound, String> v1_8 = server("LEVEL_UP", "CLICK", "AMBIENCE_THUNDER", "CHEST_OPEN", "ANVIL_LAND");
        assertEquals("LEVEL_UP", v1_8.get(EranoSound.ENTITY_PLAYER_LEVELUP));
        assertEquals("CLICK", v1_8.get(EranoSound.UI_BUTTON_CLICK));
        assertEquals("AMBIENCE_THUNDER", v1_8.get(EranoSound.ENTITY_LIGHTNING_BOLT_THUNDER));
        assertEquals("CHEST_OPEN", v1_8.get(EranoSound.BLOCK_CHEST_OPEN));
        assertEquals("ANVIL_LAND", v1_8.get(EranoSound.BLOCK_ANVIL_PLACE));
        assertNull(v1_8.get(EranoSound.ENTITY_WARDEN_ROAR));
        ServerNames<EranoSound, String> v1_12 = server("ENTITY_LIGHTNING_THUNDER", "BLOCK_NOTE_PLING", "ENTITY_ENDERMEN_TELEPORT");
        assertEquals("ENTITY_LIGHTNING_THUNDER", v1_12.get(EranoSound.ENTITY_LIGHTNING_BOLT_THUNDER));
        assertEquals("BLOCK_NOTE_PLING", v1_12.get(EranoSound.BLOCK_NOTE_BLOCK_PLING));
        assertEquals("ENTITY_ENDERMEN_TELEPORT", v1_12.get(EranoSound.ENTITY_ENDERMAN_TELEPORT));
    }

    @Test
    void reverseLookupPrefersTheSoundTheNameMeans() {
        // On 1.8 both are ENDERDRAGON_GROWL; the name means the growl
        ServerNames<EranoSound, String> v1_8 = server("ENDERDRAGON_GROWL");
        assertEquals(EranoSound.ENTITY_ENDER_DRAGON_GROWL, v1_8.of("ENDERDRAGON_GROWL"));
    }

    @Test
    void matchesNamesOldNamesAndKeys() {
        assertEquals(EranoSound.ENTITY_PLAYER_LEVELUP, EranoSound.match("LEVEL_UP").get());
        assertEquals(EranoSound.ENTITY_PLAYER_LEVELUP, EranoSound.match("minecraft:entity.player.levelup").get());
        assertEquals(EranoSound.UI_BUTTON_CLICK, EranoSound.match("click").get());
        assertEquals(EranoSound.BLOCK_NOTE_BLOCK_PLING, EranoSound.match("BLOCK_NOTE_PLING").get());
    }
}
