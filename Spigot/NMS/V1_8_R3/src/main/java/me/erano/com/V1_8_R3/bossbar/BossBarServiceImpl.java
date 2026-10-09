package me.erano.com.V1_8_R3.bossbar;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.erano.com.api.bossbar.FakeWither;
import me.erano.com.api.bossbar.PacketBossBarService;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;

public class BossBarServiceImpl extends PacketBossBarService {

    BossBarServiceImpl(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected FakeWither newWither(World world) {
        return new FakeWitherImpl(world);
    }

    @Override
    protected void sendDestroy(Player player, int entityId) {
        FakeWitherImpl.connection(player).sendPacket(new PacketPlayOutEntityDestroy(entityId));
    }
}
