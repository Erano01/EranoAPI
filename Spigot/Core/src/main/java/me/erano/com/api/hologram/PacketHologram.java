package me.erano.com.api.hologram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class PacketHologram extends AbstractHologram {

    private final PacketHologramService service;
    private final List<PacketLine> packetLines = new ArrayList<>();
    /** Viewers whose client currently has the lines. */
    private final Set<UUID> shown = new HashSet<>();

    PacketHologram(PacketHologramService service, Location location, List<String> lines) {
        super(service, location, lines);
        this.service = service;
    }

    @Override
    protected void spawn() {
        packetLines.clear();
        for (int i = 0; i < lines.size(); i++) {
            PacketLine line = service.newLine(location.getWorld());
            line.set(lineLocation(i), lines.get(i));
            packetLines.add(line);
        }
    }

    @Override
    protected void tick() {
        for (Player viewer : viewers()) {
            update(viewer);
        }
    }

    private void update(Player viewer) {
        boolean inRange = viewer.getWorld().equals(location.getWorld())
                && viewer.getLocation().distanceSquared(location) <= service.viewRange() * service.viewRange();
        boolean isShown = shown.contains(viewer.getUniqueId());
        if (inRange && !isShown) {
            // In case the client still has an old copy.
            service.sendDestroy(viewer, entityIds());
            for (PacketLine line : packetLines) {
                line.sendSpawn(viewer);
            }
            shown.add(viewer.getUniqueId());
        } else if (!inRange && isShown) {
            hide(viewer);
        }
    }

    private void hide(Player viewer) {
        if (shown.remove(viewer.getUniqueId()) && viewer.getWorld().equals(location.getWorld())) {
            service.sendDestroy(viewer, entityIds());
        }
    }

    @Override
    protected void linesChanged() {
        if (packetLines.size() == lines.size()) {
            for (int i = 0; i < lines.size(); i++) {
                packetLines.get(i).set(lineLocation(i), lines.get(i));
                for (Player viewer : shownViewers()) {
                    packetLines.get(i).sendMetadata(viewer);
                }
            }
            return;
        }
        respawn();
    }

    @Override
    protected void moved(Location from) {
        if (!from.getWorld().equals(location.getWorld())) {
            respawn();
            return;
        }
        for (int i = 0; i < packetLines.size(); i++) {
            packetLines.get(i).set(lineLocation(i), lines.get(i));
            for (Player viewer : shownViewers()) {
                packetLines.get(i).sendTeleport(viewer);
            }
        }
        tick();
    }

    private void respawn() {
        despawn();
        spawn();
        tick();
    }

    @Override
    protected void viewerAdded(Player player) {
        update(player);
    }

    @Override
    protected void viewerRemoved(Player player) {
        hide(player);
    }

    @Override
    protected void playerReset(Player player) {
        shown.remove(player.getUniqueId());
    }

    @Override
    protected void despawn() {
        for (Player viewer : shownViewers()) {
            service.sendDestroy(viewer, entityIds());
        }
        shown.clear();
    }

    private List<Player> shownViewers() {
        List<Player> players = new ArrayList<>();
        for (UUID id : shown) {
            Player player = Bukkit.getPlayer(id);
            if (player != null) {
                players.add(player);
            }
        }
        return players;
    }

    private int[] entityIds() {
        int[] ids = new int[packetLines.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = packetLines.get(i).entityId();
        }
        return ids;
    }
}
