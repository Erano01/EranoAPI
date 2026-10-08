package me.erano.com.api.hologram;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * A hologram made of non-persistent entities. They disappear with their chunk; {@link #tick()} spawns them
 * again once the chunk is loaded.
 */
abstract class EntityHologram extends AbstractHologram {

    private final EntityHologramService service;
    private final List<Entity> entities = new ArrayList<>();

    EntityHologram(EntityHologramService service, Location location, List<String> lines) {
        super(service, location, lines);
        this.service = service;
    }

    /** Spawns the entities at {@link #location}, already hidden when {@link EntityHologramService.Visibility#HIDDEN_BY_DEFAULT}. */
    protected abstract List<Entity> spawnEntities(boolean hiddenByDefault);

    /** Updates the spawned entities for new {@link #lines}; false to respawn them instead. */
    protected abstract boolean updateLines(List<Entity> entities);

    @Override
    protected void spawn() {
        if (!location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
            return;
        }
        entities.addAll(spawnEntities(service.visibility() == EntityHologramService.Visibility.HIDDEN_BY_DEFAULT));
        switch (service.visibility()) {
            case HIDDEN_BY_DEFAULT:
                for (Player viewer : viewers()) {
                    setVisible(viewer, true);
                }
                break;
            case HIDE_FROM_OTHERS:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!isViewer(player)) {
                        setVisible(player, false);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void tick() {
        boolean intact = !entities.isEmpty();
        for (Entity entity : entities) {
            intact &= entity.isValid();
        }
        if (!intact) {
            despawn();
            spawn();
        }
    }

    @Override
    protected void linesChanged() {
        if (entities.isEmpty() || !updateLines(entities)) {
            despawn();
            spawn();
        }
    }

    @Override
    protected void moved(Location from) {
        despawn();
        spawn();
    }

    @Override
    protected void viewerAdded(Player player) {
        setVisible(player, true);
    }

    @Override
    protected void viewerRemoved(Player player) {
        if (service.visibility() != EntityHologramService.Visibility.EVERYONE) {
            setVisible(player, false);
        }
    }

    @Override
    protected void playerJoined(Player player) {
        if (service.visibility() == EntityHologramService.Visibility.HIDE_FROM_OTHERS && !isViewer(player)) {
            setVisible(player, false);
        }
    }

    @Override
    protected void despawn() {
        for (Entity entity : entities) {
            entity.remove();
        }
        entities.clear();
    }

    private void setVisible(Player player, boolean visible) {
        if (service.visibility() == EntityHologramService.Visibility.EVERYONE) {
            return;
        }
        for (Entity entity : entities) {
            if (visible) {
                player.showEntity(service.plugin(), entity);
            } else {
                player.hideEntity(service.plugin(), entity);
            }
        }
    }
}
