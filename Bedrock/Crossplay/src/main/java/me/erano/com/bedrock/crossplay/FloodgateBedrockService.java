package me.erano.com.bedrock.crossplay;

import java.util.UUID;

import org.bukkit.plugin.Plugin;
import org.geysermc.cumulus.form.Form;
import org.geysermc.floodgate.api.FloodgateApi;

/** Floodgate: works on every backend, also when Geyser runs on the proxy. */
class FloodgateBedrockService extends FormBedrockService {

    FloodgateBedrockService(Plugin plugin) {
        super(plugin);
    }

    @Override
    boolean isBedrock(UUID player) {
        return FloodgateApi.getInstance().isFloodgatePlayer(player);
    }

    @Override
    void send(UUID player, Form form) {
        FloodgateApi.getInstance().sendForm(player, form);
    }
}
