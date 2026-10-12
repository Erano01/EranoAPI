package me.erano.com.api.bedrock;

import org.bukkit.plugin.Plugin;

import me.erano.com.common.VersionRange;

/** Every version: loses to Bedrock/Crossplay's providers where Geyser / Floodgate is installed. */
public class NoBedrockServiceProvider implements BedrockServiceProvider {

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.atLeast("1.8");
    }

    @Override
    public BedrockService create(Plugin plugin) {
        return new NoBedrockService();
    }
}
