package me.erano.com.api.hologram;

import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Creates {@link Hologram}s. Get it from {@link me.erano.com.api.EranoServices#holograms()}.
 *
 * <p>Holograms are never written to the world, so a crash can't leave them behind. Still call
 * {@link Hologram#remove()} (or {@link #removeAll()}) before copying a loaded world and in your plugin's
 * {@code onDisable}; EranoAPI removes everything left when it is disabled itself.
 */
public interface HologramService {

    Hologram show(Location at, List<String> lines, Collection<? extends Player> viewers);

    void removeAll();

    /** False only on versions without any implementation; {@link #show} then returns a hologram that does nothing. */
    default boolean isSupported() {
        return true;
    }

    /** Whether only viewers see a hologram; false on 1.13 - 1.17. See {@link Hologram}. */
    boolean isPerViewer();
}
