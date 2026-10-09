package me.erano.com.api.bossbar;

import java.util.Collection;

import org.bukkit.entity.Player;

/** One bar of {@link BossBarService}. Main thread only. */
public interface GameBar {

    String title();

    void setTitle(String title);

    /** 0 (empty) to 1 (full); values outside are clamped. */
    double progress();

    void setProgress(double progress);

    void addPlayer(Player player);

    void removePlayer(Player player);

    /** The players it's shown to, online. */
    Collection<Player> players();

    /** Hides it from everyone; it can be shown again with {@link #addPlayer}. */
    void removeAll();
}
