package me.erano.com.V1_8_R3.hologram;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.erano.com.api.hologram.PacketLine;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PlayerConnection;

/** An invisible armor stand that is never added to the world; only its packets are sent. */
class PacketLineImpl implements PacketLine {

    private static final double NAME_OFFSET = 0;

    private final EntityArmorStand stand;

    PacketLineImpl(World world) {
        stand = new EntityArmorStand(((CraftWorld) world).getHandle());
        stand.setInvisible(true);
        stand.setGravity(false);
        stand.setBasePlate(false);
        stand.n(true);
    }

    @Override
    public int entityId() {
        return stand.getId();
    }

    @Override
    public void set(Location at, String text) {
        stand.setLocation(at.getX(), at.getY() - NAME_OFFSET, at.getZ(), 0, 0);
        stand.setCustomName(text);
        stand.setCustomNameVisible(!text.isEmpty());
    }

    @Override
    public void sendSpawn(Player player) {
        connection(player).sendPacket(new PacketPlayOutSpawnEntityLiving(stand));
    }

    @Override
    public void sendMetadata(Player player) {
        connection(player).sendPacket(new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true));
    }

    @Override
    public void sendTeleport(Player player) {
        connection(player).sendPacket(new PacketPlayOutEntityTeleport(stand));
    }

    static PlayerConnection connection(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection;
    }
}
