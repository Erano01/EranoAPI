package me.erano.com.V1_8_R2.bossbar;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.erano.com.api.bossbar.FakeWither;
import net.minecraft.server.v1_8_R2.EntityWither;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R2.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R2.PlayerConnection;

/** An invisible wither that is never added to the world; only its packets are sent. */
class FakeWitherImpl implements FakeWither {

    private final EntityWither wither;

    FakeWitherImpl(World world) {
        wither = new EntityWither(((CraftWorld) world).getHandle());
        wither.setInvisible(true);
    }

    @Override
    public int entityId() {
        return wither.getId();
    }

    @Override
    public void set(Location at, String title, double progress) {
        wither.setLocation(at.getX(), at.getY(), at.getZ(), 0, 0);
        wither.setCustomName(title);
        // At 0 health the client takes it for dead.
        wither.setHealth((float) Math.max(1, progress * wither.getMaxHealth()));
    }

    @Override
    public void sendSpawn(Player player) {
        connection(player).sendPacket(new PacketPlayOutSpawnEntityLiving(wither));
    }

    @Override
    public void sendMetadata(Player player) {
        connection(player).sendPacket(new PacketPlayOutEntityMetadata(wither.getId(), wither.getDataWatcher(), true));
    }

    @Override
    public void sendTeleport(Player player) {
        connection(player).sendPacket(new PacketPlayOutEntityTeleport(wither));
    }

    static PlayerConnection connection(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection;
    }
}
