package me.erano.com.bedrock.crossplay;

import java.util.UUID;

import org.bukkit.plugin.Plugin;
import org.geysermc.cumulus.form.Form;
import org.geysermc.geyser.api.GeyserApi;

/** Geyser without Floodgate: only knows the players of the Geyser running on this server. */
class GeyserBedrockService extends FormBedrockService {

    GeyserBedrockService(Plugin plugin) {
        super(plugin);
    }

    @Override
    boolean isBedrock(UUID player) {
        return GeyserApi.api().isBedrockPlayer(player);
    }

    @Override
    void send(UUID player, Form form) {
        GeyserApi.api().sendForm(player, form);
    }
}
