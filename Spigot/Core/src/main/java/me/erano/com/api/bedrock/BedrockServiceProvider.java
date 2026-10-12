package me.erano.com.api.bedrock;

import org.bukkit.plugin.Plugin;

import me.erano.com.common.VersionedService;

/** SPI for {@link BedrockService}; see {@link VersionedService} for the rules. */
public interface BedrockServiceProvider extends VersionedService {

    /** @param plugin owner of the tasks the service schedules */
    BedrockService create(Plugin plugin);
}
