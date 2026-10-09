package me.erano.com.api.bossbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/** Used when no provider supports the server version: bars that keep their state but show nothing. */
public final class UnsupportedBossBarService implements BossBarService {

    @Override
    public GameBar create(final String title, String color) {
        return new GameBar() {
            private final Set<UUID> viewers = new LinkedHashSet<>();
            private String current = title;
            private double progress = 1;

            @Override
            public String title() {
                return current;
            }

            @Override
            public void setTitle(String title) {
                current = title;
            }

            @Override
            public double progress() {
                return progress;
            }

            @Override
            public void setProgress(double progress) {
                this.progress = Math.max(0, Math.min(1, progress));
            }

            @Override
            public void addPlayer(Player player) {
                viewers.add(player.getUniqueId());
            }

            @Override
            public void removePlayer(Player player) {
                viewers.remove(player.getUniqueId());
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
                viewers.clear();
            }
        };
    }

    @Override
    public boolean isSupported() {
        return false;
    }

    @Override
    public boolean isLegacy() {
        return false;
    }
}
