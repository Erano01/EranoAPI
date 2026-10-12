package me.erano.com.bedrock.crossplay;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import me.erano.com.api.bedrock.BedrockService;
import me.erano.com.api.bedrock.BedrockServiceProvider;
import me.erano.com.common.VersionRange;

/** Where Floodgate is installed; preferred over Geyser's API, see {@link FloodgateBedrockService}. */
public class FloodgateBedrockServiceProvider implements BedrockServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.atLeast("1.8");
    }

    @Override
    public boolean isAvailable() {
        return Bukkit.getPluginManager().isPluginEnabled("floodgate");
    }

    @Override
    public int priority() {
        return 2;
    }

    @Override
    public BedrockService create(Plugin plugin) {
        return new FloodgateBedrockService(plugin);
    }
}
