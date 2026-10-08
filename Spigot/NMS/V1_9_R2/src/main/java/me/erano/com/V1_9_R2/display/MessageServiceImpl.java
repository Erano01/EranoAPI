package me.erano.com.V1_9_R2.display;

import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.erano.com.api.display.BukkitMessageService;
import net.minecraft.server.v1_9_R2.ChatComponentText;
import net.minecraft.server.v1_9_R2.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_9_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R2.PlayerConnection;

/** The action bar API exists since 1.9, title timings only since 1.11: titles are packets here. */
public class MessageServiceImpl extends BukkitMessageService {

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
}
