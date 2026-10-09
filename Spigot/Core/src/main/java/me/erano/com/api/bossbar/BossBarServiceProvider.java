package me.erano.com.api.bossbar;

import org.bukkit.plugin.Plugin;

import me.erano.com.common.VersionedService;

/** SPI for {@link BossBarService}; see {@link VersionedService} for the rules. */
public interface BossBarServiceProvider extends VersionedService {

    /** @param plugin owner of the listeners and tasks the service registers */
    BossBarService create(Plugin plugin);
}
