package me.erano.com.api.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * One line of a {@link PacketHologramService} hologram: an armor stand that exists only on the clients it
 * was sent to. Implemented by the NMS modules.
 */
public interface PacketLine {

    int entityId();

    /** Updates the server side copy; nothing is sent. Empty text hides the name. */
    void set(Location at, String text);

    void sendSpawn(Player player);

    /** Sends the current name. */
    void sendMetadata(Player player);

    /** Sends the current position. */
    void sendTeleport(Player player);
}
