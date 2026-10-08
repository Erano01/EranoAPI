package me.erano.com.api.hologram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/** Used when no provider supports the server version: holograms that keep their state but show nothing. */
public final class UnsupportedHologramService implements HologramService {

    @Override
    public Hologram show(Location at, List<String> lines, Collection<? extends Player> viewers) {
        return new Hologram() {
            private Location location = at.clone();
            private List<String> current = new ArrayList<>(lines);
            private boolean removed;

            @Override
            public void setLines(List<String> lines) {
                current = new ArrayList<>(lines);
            }

            @Override
            public List<String> lines() {
                return Collections.unmodifiableList(current);
            }

            @Override
            public Location location() {
                return location.clone();
            }

            @Override
            public void teleport(Location to) {
                location = to.clone();
            }

            @Override
            public void addViewer(Player player) {
            }

            @Override
            public void removeViewer(Player player) {
            }

            @Override
            public Collection<Player> viewers() {
                return Collections.emptyList();
            }

            @Override
            public void remove() {
                removed = true;
            }

            @Override
            public boolean isRemoved() {
                return removed;
            }
        };
    }

    @Override
    public void removeAll() {
    }

    @Override
    public boolean isSupported() {
        return false;
    }

    @Override
    public boolean isPerViewer() {
        return false;
    }
}
