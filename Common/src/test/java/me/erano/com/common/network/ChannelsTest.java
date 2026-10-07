package me.erano.com.common.network;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ChannelsTest {

    @Test
    void acceptsNamespacedLowercase() {
        assertEquals("hungergames:hello_ack", Channels.validate("hungergames:hello_ack"));
        assertEquals("eranoapi:a/b.c-d", Channels.validate("eranoapi:a/b.c-d"));
    }

    @Test
    void rejectsLegacyAndUppercaseNames() {
        assertThrows(IllegalArgumentException.class, () -> Channels.validate("BungeeCord"));
        assertThrows(IllegalArgumentException.class, () -> Channels.validate("HungerGames:hello"));
        assertThrows(IllegalArgumentException.class, () -> Channels.validate("hungergames:"));
        assertThrows(IllegalArgumentException.class, () -> Channels.validate(null));
    }
}
