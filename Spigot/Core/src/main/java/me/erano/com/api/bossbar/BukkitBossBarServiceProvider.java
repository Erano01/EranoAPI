package me.erano.com.api.bossbar;

import org.bukkit.plugin.Plugin;

import me.erano.com.common.VersionRange;

/** 1.9+, Bukkit API only (boss bars came in 1.9). */
public class BukkitBossBarServiceProvider implements BossBarServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.atLeast("1.9");
    }

    @Override
    public BossBarService create(Plugin plugin) {
        return new BukkitBossBarService();
    }
}
