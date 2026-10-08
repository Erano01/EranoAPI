package me.erano.com.api.hologram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * Bookkeeping shared by every implementation: the live holograms, a once a second {@link AbstractHologram#tick()}
 * and the player events they react to.
 */
public abstract class AbstractHologramService implements HologramService, Listener {

    protected final Plugin plugin;
    private final Set<AbstractHologram> holograms = new LinkedHashSet<>();
    private final BukkitTask task;

    protected AbstractHologramService(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 20L, 20L);
    }

    protected abstract AbstractHologram create(Location at, List<String> lines);

    @Override
    public Hologram show(Location at, List<String> lines, Collection<? extends Player> viewers) {
        AbstractHologram hologram = create(at.clone(), new ArrayList<>(lines));
        holograms.add(hologram);
        hologram.spawn();
        for (Player viewer : viewers) {
            hologram.addViewer(viewer);
        }
        return hologram;
    }

    @Override
    public void removeAll() {
        for (AbstractHologram hologram : new ArrayList<>(holograms)) {
            hologram.remove();
        }
    }

    /** Removes every hologram and stops listening; called when EranoAPI is disabled. */
    public void shutdown() {
        removeAll();
        task.cancel();
        HandlerList.unregisterAll(this);
    }

    void unregister(AbstractHologram hologram) {
        holograms.remove(hologram);
    }

    private void tick() {
        for (AbstractHologram hologram : new ArrayList<>(holograms)) {
            hologram.tick();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        for (AbstractHologram hologram : holograms) {
            hologram.playerJoined(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        for (AbstractHologram hologram : holograms) {
            hologram.removeViewer(event.getPlayer());
        }
    }

    // The client drops its entities on world change and respawn, and when a teleport unloads the chunk.
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        playerReset(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChangedWorld(PlayerChangedWorldEvent event) {
        playerReset(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent event) {
        playerReset(event.getPlayer());
    }

    private void playerReset(Player player) {
        for (AbstractHologram hologram : holograms) {
            hologram.playerReset(player);
        }
    }
}
