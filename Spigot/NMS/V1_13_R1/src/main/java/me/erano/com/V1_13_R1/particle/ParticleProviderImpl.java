package me.erano.com.V1_13_R1.particle;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.erano.com.api.particle.IParticleProvider;
import me.erano.com.api.particle.ParticleEffect;

/**
 * 1.13 (V1_13_R1) için Particle SPI implementasyonu
 * Design Pattern: Bridge (abstraction-implementation separation)
 */
public class ParticleProviderImpl implements IParticleProvider {
    @Override
    public void spawnParticle(World world, Location location, ParticleEffect effect, int count,
                             double offsetX, double offsetY, double offsetZ, double speed, Object data, Player... receivers) {
        // 1.13_R1 için merkezi mapping ile NMS partikül seçimi
        Object nmsParticle = null;
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
        net.minecraft.server.v1_13_R1.PacketPlayOutWorldParticles packet =
                new net.minecraft.server.v1_13_R1.PacketPlayOutWorldParticles(
                        (net.minecraft.server.v1_13_R1.ParticleParam) nmsParticle,
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
        //1.13-1.18 (in these version particles NMS&OBC interfaces are changed -> 1.8, 1.13 and 1.19)
        if (version.startsWith("1.13") || version.startsWith("1.14")
                || version.startsWith("1.15")|| version.startsWith("1.16")
                || version.startsWith("1.17") || version.startsWith("1.18")){
            return true;
        }
        return false;
    }

    /**
     * 1.13 ile birlikte ParticleType enumları kaldırıldı, Particle ve ParticleParam yapısı geldi.
     * FLAME gibi partiküller için Particles.FLAME, REDSTONE için ParticleParamRedstone kullanılır.
     * Diğer partiküller için uygun parametreler eklenmeli.
     *
     * Obfuscated enum ve parametre isimleri için: https://mappings.dev/ veya Spigot kaynak kodu incelenmeli.
     *
     * @param effect ParticleEffect abstraction
     * @param data REDSTONE için renk/size gibi ek parametreler (opsiyonel)
     */
    private net.minecraft.server.v1_13_R1.ParticleParam toNMSParticle(ParticleEffect effect, Object data) {
        switch (effect) {
            case FLAME:
                // 1.13'te FLAME için Particles enumunda doğrudan FLAME yoksa,
                // reflection ile field erişimi yapılır. (Obfuscated API farkı)
                // https://mappings.dev/ veya Spigot kaynak kodu referans alınabilir.
                try {
                    java.lang.reflect.Field field = net.minecraft.server.v1_13_R1.Particles.class.getDeclaredField("FLAME");
                    field.setAccessible(true);
                    return (net.minecraft.server.v1_13_R1.ParticleParam) field.get(null);
                } catch (Exception e) {
                    // Eğer field yoksa veya hata olursa null döner
                    return null;
                }
            case REDSTONE:
                // REDSTONE için ParticleParamRedstone kullanılır (r,g,b,size)
                float r = 1.0F, g = 0.0F, b = 0.0F, size = 1.0F;
                if (data instanceof float[] && ((float[]) data).length >= 4) {
                    float[] arr = (float[]) data;
                    r = arr[0]; g = arr[1]; b = arr[2]; size = arr[3];
                }
                return new net.minecraft.server.v1_13_R1.ParticleParamRedstone(r, g, b, size);
            // Diğer partiküller için de benzer şekilde eklenebilir
            default:
                return null;
        }
    }
}
