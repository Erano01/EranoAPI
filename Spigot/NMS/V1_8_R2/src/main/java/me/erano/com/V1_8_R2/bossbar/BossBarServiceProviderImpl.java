package me.erano.com.V1_8_R2.bossbar;

import org.bukkit.plugin.Plugin;

import me.erano.com.api.bossbar.BossBarService;
import me.erano.com.api.bossbar.BossBarServiceProvider;
import me.erano.com.common.VersionRange;

public class BossBarServiceProviderImpl implements BossBarServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.only("1.8.3");
    }

    @Override
    public BossBarService create(Plugin plugin) {
        return new BossBarServiceImpl(plugin);
    }
}
