package me.erano.com.api.performance;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public interface TPSHandler {

	// Even in 1.20 API, you need NMS for this
    double[] getTPS();

    /** @deprecated use {@link me.erano.com.api.EranoServices#messages()}, which works on every version */
    @Deprecated
    default void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
}
