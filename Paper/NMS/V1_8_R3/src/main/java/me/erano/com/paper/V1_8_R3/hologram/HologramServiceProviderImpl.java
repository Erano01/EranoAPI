package me.erano.com.paper.V1_8_R3.hologram;

import org.bukkit.plugin.Plugin;

import me.erano.com.api.Platform;
import me.erano.com.api.hologram.HologramService;
import me.erano.com.api.hologram.HologramServiceProvider;
import me.erano.com.common.VersionRange;

public class HologramServiceProviderImpl implements HologramServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.between("1.8.4", "1.8.9");
    }

    @Override
    public HologramService create(Plugin plugin) {
        return new HologramServiceImpl(plugin);
    }

    @Override
    public boolean isAvailable() {
        return Platform.isPaper();
    }

    @Override
    public int priority() {
        return 1;
    }
}
