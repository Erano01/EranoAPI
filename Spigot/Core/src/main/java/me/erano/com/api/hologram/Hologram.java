package me.erano.com.api.hologram;

import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Text lines that only the viewers see and that are never saved to the world: packets on 1.8 - 1.12,
 * non-persistent entities on 1.13+. Main thread only.
 *
 * <p>Visibility per viewer: exact on 1.8 - 1.12 and 1.19.3+ (nobody else gets a packet), close on
 * 1.18 - 1.19.2 (spawned for everyone, then hidden from non-viewers), not available on 1.13 - 1.17
 * (everyone in range sees it). See {@link HologramService#isPerViewer()}.
 */
public interface Hologram {

    /** Top line first; an empty string is a blank line. */
    void setLines(List<String> lines);

    List<String> lines();

    /** Position of the top line. */
    Location location();

    void teleport(Location to);

    void addViewer(Player player);

    void removeViewer(Player player);

    /** Online viewers. Players are removed when they quit. */
    Collection<Player> viewers();

    /** Despawns it for every viewer. Further calls (other than {@link #remove()}) throw. */
    void remove();

    boolean isRemoved();
}
