package me.erano.com.paper.V1_8_R3.bossbar;

import org.bukkit.plugin.Plugin;

import me.erano.com.api.Platform;
import me.erano.com.api.bossbar.BossBarService;
import me.erano.com.api.bossbar.BossBarServiceProvider;
import me.erano.com.common.VersionRange;

public class BossBarServiceProviderImpl implements BossBarServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.between("1.8.4", "1.8.9");
    }

    @Override
    public BossBarService create(Plugin plugin) {
        return new BossBarServiceImpl(plugin);
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
