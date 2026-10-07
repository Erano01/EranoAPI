package me.erano.com.V1_19_R1.particle;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import me.erano.com.api.particle.IParticleProvider;
import me.erano.com.api.particle.ParticleEffect;

/**
 * 1.19_R1 için Particle SPI implementasyonu
 * Design Pattern: Bridge, SPI, Centralized Mapping
 */
public class ParticleProviderImpl implements IParticleProvider {
    @Override
    public void spawnParticle(World world, Location location, ParticleEffect effect, int count,
                             double offsetX, double offsetY, double offsetZ, double speed, Object data, Player... receivers) {
        Object nmsParticle = null;
        switch (effect) {
            case FLAME:
                nmsParticle = ParticleMapping.FLAME;
                break;
            case REDSTONE:
                nmsParticle = ParticleMapping.REDSTONE;
                break;
            default:
                nmsParticle = null;
        }
        if (nmsParticle == null) return;
        net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket packet =
                new net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket(
                        (net.minecraft.core.particles.ParticleOptions) nmsParticle,
                        true,
                        (float) location.getX(),
                        (float) location.getY(),
                        (float) location.getZ(),
                        (float) offsetX,
                        (float) offsetY,
                        (float) offsetZ,
                        (float) speed,
                        count
                );
        if (receivers != null && receivers.length > 0) {
            for (Player player : receivers) {
                try {
                    ((CraftPlayer) player).getHandle().connection.send(packet);
                } catch (Exception e) {
                    // Fallback: reflection ile send/sendPacket/a() methodunu bul ve çağır
                    try {
                        Object connection = ((CraftPlayer) player).getHandle().connection;
                        java.lang.reflect.Method sendMethod = null;
                        for (java.lang.reflect.Method m : connection.getClass().getMethods()) {
                            if (m.getName().equals("send") || m.getName().equals("sendPacket") || m.getName().equals("a")) {
                                sendMethod = m;
                                break;
                            }
                        }
                        if (sendMethod != null) {
                            sendMethod.invoke(connection, packet);
                        }
                    } catch (Exception ignored) {}
                }
            }
        } else {
            for (Player player : world.getPlayers()) {
                try {
                    ((CraftPlayer) player).getHandle().connection.send(packet);
                } catch (Exception e) {
                    try {
                        Object connection = ((CraftPlayer) player).getHandle().connection;
                        java.lang.reflect.Method sendMethod = null;
                        for (java.lang.reflect.Method m : connection.getClass().getMethods()) {
                            if (m.getName().equals("send") || m.getName().equals("sendPacket") || m.getName().equals("a")) {
                                sendMethod = m;
                                break;
                            }
                        }
                        if (sendMethod != null) {
                            sendMethod.invoke(connection, packet);
                        }
                    } catch (Exception ignored) {}
                }
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
        //1.19+
        if (version.startsWith("1.19") || version.startsWith("1.20")
                || version.startsWith("1.21")){
            return true;
        }
        return false;
    }
}
