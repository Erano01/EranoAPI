package me.erano.com.api.bedrock;

import org.bukkit.entity.Player;

import me.erano.com.common.menu.dialog.DialogService;

/**
 * Bedrock players on this Java server, through Geyser / Floodgate (Bedrock/Crossplay). Get it from
 * {@link me.erano.com.api.EranoServices#bedrock()}.
 */
public interface BedrockService {

    boolean isBedrock(Player player);

    /** Shows {@code DialogMenu}s to Bedrock players as forms. */
    DialogService<Player> forms();

    /** False without Geyser / Floodgate: then nobody is a Bedrock player. */
    default boolean isSupported() {
        return true;
    }
}
