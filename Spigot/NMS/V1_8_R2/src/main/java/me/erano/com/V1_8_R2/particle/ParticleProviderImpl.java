package me.erano.com.V1_8_R2.particle;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.erano.com.api.particle.IParticleProvider;
import me.erano.com.api.particle.ParticleEffect;

/**
 * 1.8.3 (V1_8_R2) için Particle SPI implementasyonu
 * Design Pattern: Bridge (abstraction-implementation separation)
 */
public class ParticleProviderImpl implements IParticleProvider {
    @Override
    public void spawnParticle(World world, Location location, ParticleEffect effect, int count,
                             double offsetX, double offsetY, double offsetZ, double speed, Object data, Player... receivers) {
        // 1.8.3'da NMS particle mapping merkezi ParticleMapping class'ı ile yapılır.
        Object nmsParticle = ParticleMapping.FLAME;
        switch (effect) {
            case FLAME:
                nmsParticle = ParticleMapping.FLAME;
                break;
            case REDSTONE:
                nmsParticle = ParticleMapping.REDSTONE;
                break;
            // Diğer partiküller için mapping eklenebilir
            default:
                nmsParticle = null;
        }
        if (nmsParticle == null) return;
        net.minecraft.server.v1_8_R2.PacketPlayOutWorldParticles packet =
                new net.minecraft.server.v1_8_R2.PacketPlayOutWorldParticles(
                        (net.minecraft.server.v1_8_R2.EnumParticle) nmsParticle,
                        true,
                        (float) location.getX(),
                        (float) location.getY(),
                        (float) location.getZ(),
                        (float) offsetX,
                        (float) offsetY,
                        (float) offsetZ,
                        (float) speed,
                        count,
                        new int[0]
                );
        if (receivers != null && receivers.length > 0) {
            for (Player player : receivers) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        } else {
            for (Player player : world.getPlayers()) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    @Override
    public boolean isSupported(ParticleEffect effect) {
        switch (effect) {
            case FLAME: return ParticleMapping.FLAME != null;
            case REDSTONE: return ParticleMapping.REDSTONE != null;
            default: return false;
        }
    }

    @Override
    public boolean supportsVersion(String version) {
        if (version.equals("1.8.3")) {
            return true;
        }
        return false;
    }

    private net.minecraft.server.v1_8_R2.EnumParticle toNMSParticle(ParticleEffect effect) {
        switch (effect) {
            case FLAME: return net.minecraft.server.v1_8_R2.EnumParticle.FLAME;
            case REDSTONE: return net.minecraft.server.v1_8_R2.EnumParticle.REDSTONE;
            case BLOCK_CRACK: return net.minecraft.server.v1_8_R2.EnumParticle.BLOCK_CRACK;
            case BLOCK_DUST: return net.minecraft.server.v1_8_R2.EnumParticle.BLOCK_DUST;
            // Diğerleri eklenebilir
            default:
                try {
                    return net.minecraft.server.v1_8_R2.EnumParticle.valueOf(effect.name());
                } catch (Exception e) {
                    return null;
                }
        }
    }
}
