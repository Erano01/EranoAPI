package me.erano.com.api.display;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Action bar through {@code Player.Spigot#sendMessage(ChatMessageType, ...)} (1.9+) and titles through
 * {@link Player#sendTitle(String, String, int, int, int)} (1.11+). 1.9 - 1.10 subclasses override
 * {@link #sendTitle} with packets.
 */
public class BukkitMessageService implements MessageService {

    @Override
    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(orEmpty(message)));
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(orEmpty(title), orEmpty(subtitle), fadeIn, stay, fadeOut);
    }

    protected static String orEmpty(String text) {
        return text == null ? "" : text;
    }
}
