package me.erano.com.api.hologram;

import org.bukkit.plugin.Plugin;

import me.erano.com.common.VersionedService;

/** SPI for {@link HologramService}; see {@link me.erano.com.common.VersionedService} for the rules. */
public interface HologramServiceProvider extends VersionedService {

    /** @param plugin owner of the listeners and tasks the service registers */
    HologramService create(Plugin plugin);
}
