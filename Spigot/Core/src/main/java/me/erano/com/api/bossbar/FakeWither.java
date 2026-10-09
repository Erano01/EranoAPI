package me.erano.com.api.bossbar;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * An invisible wither that exists only on the client it was sent to; its name and health are the 1.8 client's boss
 * bar. Implemented by the 1.8 NMS modules.
 */
public interface FakeWither {

    int entityId();

    /** Updates the server side copy; nothing is sent. {@code progress} 0 - 1 is its health. */
    void set(Location at, String title, double progress);

    /** Spawns it with its name and health. */
    void sendSpawn(Player player);

    /** Sends the current name and health. */
    void sendMetadata(Player player);

    /** Sends the current position. */
    void sendTeleport(Player player);
}
