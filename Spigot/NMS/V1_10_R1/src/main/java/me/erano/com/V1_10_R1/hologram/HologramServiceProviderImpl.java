package me.erano.com.V1_10_R1.hologram;

import org.bukkit.plugin.Plugin;

import me.erano.com.api.hologram.HologramService;
import me.erano.com.api.hologram.HologramServiceProvider;
import me.erano.com.common.VersionRange;

public class HologramServiceProviderImpl implements HologramServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.series("1.10");
    }

    @Override
    public HologramService create(Plugin plugin) {
        return new HologramServiceImpl(plugin);
    }
}
