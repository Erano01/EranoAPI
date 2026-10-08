package me.erano.com.V1_12_R1.hologram;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.erano.com.api.hologram.PacketHologramService;
import me.erano.com.api.hologram.PacketLine;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;

public class HologramServiceImpl extends PacketHologramService {

    HologramServiceImpl(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected PacketLine newLine(World world) {
        return new PacketLineImpl(world);
    }

    @Override
    protected void sendDestroy(Player player, int[] entityIds) {
        PacketLineImpl.connection(player).sendPacket(new PacketPlayOutEntityDestroy(entityIds));
    }
}
