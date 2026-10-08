package me.erano.com.api.hologram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/** State shared by every implementation; subclasses only put it on the screen. */
public abstract class AbstractHologram implements Hologram {

    /** Vertical distance between lines. */
    protected static final double LINE_SPACING = 0.25;

    private final AbstractHologramService service;
    protected Location location;
    protected List<String> lines;
    protected final Set<UUID> viewers = new LinkedHashSet<>();
    private boolean removed;

    protected AbstractHologram(AbstractHologramService service, Location location, List<String> lines) {
        this.service = service;
        this.location = location;
        this.lines = lines;
    }

    /** Called once after construction. */
    protected abstract void spawn();

    /** Called once a second. */
    protected abstract void tick();

    protected abstract void linesChanged();

    protected abstract void moved(Location from);

    protected abstract void viewerAdded(Player player);

    protected abstract void viewerRemoved(Player player);

    protected abstract void despawn();

    protected void playerJoined(Player player) {
    }

    /** The player's client may have dropped the hologram (world change, respawn, teleport). */
    protected void playerReset(Player player) {
    }

    @Override
    public void setLines(List<String> lines) {
        checkNotRemoved();
        this.lines = new ArrayList<>(lines);
        linesChanged();
    }

    @Override
    public List<String> lines() {
        return Collections.unmodifiableList(lines);
    }

    @Override
    public Location location() {
        return location.clone();
    }

    @Override
    public void teleport(Location to) {
        checkNotRemoved();
        Location from = location;
        location = to.clone();
        moved(from);
    }

    @Override
    public void addViewer(Player player) {
        checkNotRemoved();
        if (viewers.add(player.getUniqueId())) {
            viewerAdded(player);
        }
    }

    @Override
    public void removeViewer(Player player) {
        if (!removed && viewers.remove(player.getUniqueId())) {
            viewerRemoved(player);
        }
    }

    @Override
    public Collection<Player> viewers() {
        List<Player> online = new ArrayList<>();
        for (UUID id : viewers) {
            Player player = Bukkit.getPlayer(id);
            if (player != null) {
                online.add(player);
            }
        }
        return online;
    }

    protected boolean isViewer(Player player) {
        return viewers.contains(player.getUniqueId());
    }

    /** Location of line {@code index}. */
    protected Location lineLocation(int index) {
        return location.clone().subtract(0, index * LINE_SPACING, 0);
    }

    @Override
    public void remove() {
        if (removed) {
            return;
        }
        despawn();
        removed = true;
        viewers.clear();
        service.unregister(this);
    }

    @Override
    public boolean isRemoved() {
        return removed;
    }

    private void checkNotRemoved() {
        if (removed) {
            throw new IllegalStateException("Hologram was removed");
        }
    }
}
