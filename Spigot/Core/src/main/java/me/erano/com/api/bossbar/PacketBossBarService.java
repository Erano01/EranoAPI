package me.erano.com.api.bossbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * 1.8: the client shows the health bar of a boss it renders, so each viewer gets a wither of their own, sent only to
 * them, invisible, {@value #DISTANCE} blocks ahead of their eyes and moved there every {@value #MOVE_TICKS} ticks. The
 * client shows one boss bar at a time: a player in several bars sees the one they were added to last, the next when
 * that one lets them go.
 */
public abstract class PacketBossBarService implements BossBarService, Listener {

    /** Blocks ahead of the viewer's eyes: in view, within the wither's render distance. */
    static final double DISTANCE = 32;
    static final long MOVE_TICKS = 5;
    /** The 1.8 client cuts entity names here. */
    private static final int MAX_TITLE = 64;

    private final Plugin plugin;
    /** Every bar alive, oldest first, to find the next bar of a player let go by theirs. */
    private final List<PacketBar> bars = new ArrayList<>();
    /** By viewer: the bar they see and their wither. */
    private final Map<UUID, Shown> shown = new HashMap<>();
    private final BukkitTask task;

    private static final class Shown {
        final PacketBar bar;
        final FakeWither wither;

        Shown(PacketBar bar, FakeWither wither) {
            this.bar = bar;
            this.wither = wither;
        }
    }

    protected PacketBossBarService(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, this::follow, MOVE_TICKS, MOVE_TICKS);
    }

    /** A wither in {@code world}, not sent to anyone yet. */
    protected abstract FakeWither newWither(World world);

    protected abstract void sendDestroy(Player player, int entityId);

    @Override
    public GameBar create(String title, String color) {
        PacketBar bar = new PacketBar(title);
        bars.add(bar);
        return bar;
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public boolean isLegacy() {
        return true;
    }

    private static Location ahead(Player player) {
        Location eyes = player.getEyeLocation();
        return eyes.add(eyes.getDirection().multiply(DISTANCE));
    }

    private static String cut(String title) {
        return title.length() > MAX_TITLE ? title.substring(0, MAX_TITLE) : title;
    }

    /** Shows {@code bar} to the player instead of what they saw. */
    private void display(PacketBar bar, Player player) {
        Shown old = shown.remove(player.getUniqueId());
        if (old != null) {
            sendDestroy(player, old.wither.entityId());
        }
        FakeWither wither = newWither(player.getWorld());
        wither.set(ahead(player), cut(bar.title), bar.progress);
        wither.sendSpawn(player);
        shown.put(player.getUniqueId(), new Shown(bar, wither));
    }

    /** The player's bar let them go: the latest other bar they're in, or nothing. */
    private void next(Player player) {
        Shown old = shown.remove(player.getUniqueId());
        if (old != null) {
            sendDestroy(player, old.wither.entityId());
        }
        for (int i = bars.size() - 1; i >= 0; i--) {
            if (bars.get(i).viewers.contains(player.getUniqueId())) {
                display(bars.get(i), player);
                return;
            }
        }
    }

    private void refresh(PacketBar bar) {
        for (Map.Entry<UUID, Shown> entry : shown.entrySet()) {
            if (entry.getValue().bar != bar) {
                continue;
            }
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null) {
                FakeWither wither = entry.getValue().wither;
                wither.set(ahead(player), cut(bar.title), bar.progress);
                wither.sendMetadata(player);
            }
        }
    }

    private void follow() {
        for (Map.Entry<UUID, Shown> entry : shown.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null) {
                Shown seen = entry.getValue();
                seen.wither.set(ahead(player), cut(seen.bar.title), seen.bar.progress);
                seen.wither.sendTeleport(player);
            }
        }
    }

    /** The client dropped its entities (another world, a respawn, a long teleport): the wither again, a tick later. */
    private void resend(final Player player) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Shown seen = shown.get(player.getUniqueId());
            if (seen != null && player.isOnline()) {
                display(seen.bar, player);
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        resend(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent event) {
        resend(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getFrom().getWorld().equals(event.getTo().getWorld())
                && event.getFrom().distanceSquared(event.getTo()) > DISTANCE * DISTANCE) {
            resend(event.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        shown.remove(id);
        for (PacketBar bar : bars) {
            bar.viewers.remove(id);
        }
    }

    /** EranoAPI stops: every wither goes from the clients. */
    public void shutdown() {
        task.cancel();
        HandlerList.unregisterAll(this);
        for (Map.Entry<UUID, Shown> entry : shown.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null) {
                sendDestroy(player, entry.getValue().wither.entityId());
            }
        }
        shown.clear();
        bars.clear();
    }

    private final class PacketBar implements GameBar {

        private final Set<UUID> viewers = new LinkedHashSet<>();
        private String title;
        private double progress = 1;

        PacketBar(String title) {
            this.title = title;
        }

        @Override
        public String title() {
            return title;
        }

        @Override
        public void setTitle(String title) {
            if (!title.equals(this.title)) {
                this.title = title;
                refresh(this);
            }
        }

        @Override
        public double progress() {
            return progress;
        }

        @Override
        public void setProgress(double progress) {
            double clamped = Math.max(0, Math.min(1, progress));
            if (clamped != this.progress) {
                this.progress = clamped;
                refresh(this);
            }
        }

        @Override
        public void addPlayer(Player player) {
            if (viewers.add(player.getUniqueId())) {
                // Newest first: moved to the end of the bars, then shown.
                bars.remove(this);
                bars.add(this);
                display(this, player);
            }
        }

        @Override
        public void removePlayer(Player player) {
            if (viewers.remove(player.getUniqueId())) {
                Shown seen = shown.get(player.getUniqueId());
                if (seen != null && seen.bar == this) {
                    next(player);
                }
            }
        }

        @Override
        public Collection<Player> players() {
            List<Player> players = new ArrayList<>();
            for (UUID id : viewers) {
                Player player = Bukkit.getPlayer(id);
                if (player != null) {
                    players.add(player);
                }
            }
            return players;
        }

        @Override
        public void removeAll() {
            for (Player player : players()) {
                removePlayer(player);
            }
            viewers.clear();
        }
    }
}
