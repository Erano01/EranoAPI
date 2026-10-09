package me.erano.com.api.bossbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

/** 1.9+: Bukkit's own boss bars. */
public final class BukkitBossBarService implements BossBarService {

    @Override
    public GameBar create(String title, String color) {
        final BossBar bar = Bukkit.createBossBar(title, color(color), BarStyle.SOLID);
        return new GameBar() {
            @Override
            public String title() {
                return bar.getTitle();
            }

            @Override
            public void setTitle(String title) {
                if (!title.equals(bar.getTitle())) {
                    bar.setTitle(title);
                }
            }

            @Override
            public double progress() {
                return bar.getProgress();
            }

            @Override
            public void setProgress(double progress) {
                bar.setProgress(Math.max(0, Math.min(1, progress)));
            }

            @Override
            public void addPlayer(Player player) {
                bar.addPlayer(player);
            }

            @Override
            public void removePlayer(Player player) {
                bar.removePlayer(player);
            }

            @Override
            public Collection<Player> players() {
                return new ArrayList<>(bar.getPlayers());
            }

            @Override
            public void removeAll() {
                bar.removeAll();
            }
        };
    }

    private static BarColor color(String name) {
        try {
            return BarColor.valueOf(name.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException e) {
            return BarColor.YELLOW;
        }
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public boolean isLegacy() {
        return false;
    }
}
