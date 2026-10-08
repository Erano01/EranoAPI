package me.erano.com.api.hologram;

import org.bukkit.plugin.Plugin;

import me.erano.com.common.VersionRange;

/** 1.13+, Bukkit API only ({@code Entity#setPersistent} came in 1.13). */
public class EntityHologramServiceProvider implements HologramServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.atLeast("1.13");
    }

    @Override
    public HologramService create(Plugin plugin) {
        return new EntityHologramService(plugin);
    }
}
