package me.erano.com.bedrock.crossplay;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import me.erano.com.api.bedrock.BedrockService;
import me.erano.com.api.bedrock.BedrockServiceProvider;
import me.erano.com.common.VersionRange;

/** Where Geyser runs on this server without Floodgate. */
public class GeyserBedrockServiceProvider implements BedrockServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.atLeast("1.8");
    }

    @Override
    public boolean isAvailable() {
        return Bukkit.getPluginManager().isPluginEnabled("Geyser-Spigot");
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public BedrockService create(Plugin plugin) {
        return new GeyserBedrockService(plugin);
    }
}
