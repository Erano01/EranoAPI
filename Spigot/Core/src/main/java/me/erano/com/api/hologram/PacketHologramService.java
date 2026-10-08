package me.erano.com.api.hologram;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Holograms made of packets only (1.8 - 1.12, where entities can't be kept out of the chunk file). Nothing
 * reaches non-viewers. Viewers get the lines when they come within {@link #viewRange()} and again after
 * world changes, respawns and teleports, since their client drops the entities.
 */
public abstract class PacketHologramService extends AbstractHologramService {

    protected PacketHologramService(Plugin plugin) {
        super(plugin);
    }

    /** A new line in {@code world}, not sent to anyone yet. */
    protected abstract PacketLine newLine(World world);

    protected abstract void sendDestroy(Player player, int[] entityIds);

    /** Blocks; beyond this the client may have unloaded the chunk. */
    protected double viewRange() {
        return 48;
    }

    @Override
    protected AbstractHologram create(Location at, List<String> lines) {
        return new PacketHologram(this, at, lines);
    }

    @Override
    public boolean isPerViewer() {
        return true;
    }
}
