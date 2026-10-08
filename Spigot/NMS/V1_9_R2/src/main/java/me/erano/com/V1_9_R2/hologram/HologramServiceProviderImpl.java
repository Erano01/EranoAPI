package me.erano.com.V1_9_R2.hologram;

import org.bukkit.plugin.Plugin;

import me.erano.com.api.hologram.HologramService;
import me.erano.com.api.hologram.HologramServiceProvider;
import me.erano.com.common.VersionRange;

public class HologramServiceProviderImpl implements HologramServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.only("1.9.4");
    }

    @Override
    public HologramService create(Plugin plugin) {
        return new HologramServiceImpl(plugin);
    }
}
