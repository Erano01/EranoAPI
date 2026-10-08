package me.erano.com.V1_9_R1.hologram;

import org.bukkit.plugin.Plugin;

import me.erano.com.api.hologram.HologramService;
import me.erano.com.api.hologram.HologramServiceProvider;
import me.erano.com.common.VersionRange;

public class HologramServiceProviderImpl implements HologramServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.between("1.9", "1.9.3");
    }

    @Override
    public HologramService create(Plugin plugin) {
        return new HologramServiceImpl(plugin);
    }
}
