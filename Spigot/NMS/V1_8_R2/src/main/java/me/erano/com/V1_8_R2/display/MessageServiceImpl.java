package me.erano.com.V1_8_R2.display;

import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.erano.com.api.display.MessageService;
import net.minecraft.server.v1_8_R2.ChatComponentText;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_8_R2.PacketPlayOutChat;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R2.PlayerConnection;

/** 1.8 has neither an action bar API nor title timings: both are packets. */
public class MessageServiceImpl implements MessageService {

    // Chat packet position 2 is the action bar.
    @Override
    public void sendActionBar(Player player, String message) {
        connection(player).sendPacket(new PacketPlayOutChat(new ChatComponentText(orEmpty(message)), (byte) 2));
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        PlayerConnection connection = connection(player);
        connection.sendPacket(new PacketPlayOutTitle(fadeIn, stay, fadeOut));
        connection.sendPacket(new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, new ChatComponentText(orEmpty(subtitle))));
        connection.sendPacket(new PacketPlayOutTitle(EnumTitleAction.TITLE, new ChatComponentText(orEmpty(title))));
    }

    private static PlayerConnection connection(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection;
    }

    private static String orEmpty(String text) {
        return text == null ? "" : text;
    }
}
