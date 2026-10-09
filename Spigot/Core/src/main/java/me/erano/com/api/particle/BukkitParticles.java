package me.erano.com.api.particle;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import me.erano.com.api.internal.ServerNames;
import me.erano.com.api.material.EranoMaterial;

/**
 * 1.9+: Bukkit's {@link Particle}. Only loaded there; the data conversions for one version's types live in their
 * own methods, so a type that version lacks ({@code BlockData} before 1.13) is never touched.
 */
final class BukkitParticles {

    private static volatile ServerNames<EranoParticle, Particle> server;

    private BukkitParticles() {
    }

    static Particle get(EranoParticle particle) {
        return server().get(particle);
    }

    static EranoParticle of(Particle particle) {
        return server().of(particle);
    }

    static void spawn(EranoParticle effect, Location location, int count, double offsetX, double offsetY,
                      double offsetZ, double speed, Object data, Player... receivers) {
        Particle particle = get(effect);
        if (particle == null) {
            return;
        }
        Object value = data(particle, data);
        if (receivers != null && receivers.length > 0) {
            for (Player player : receivers) {
                player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed, value);
            }
        } else if (location.getWorld() != null) {
            location.getWorld().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed, value);
        }
    }

    private static Object data(Particle particle, Object data) {
        Class<?> type = particle.getDataType();
        if (type == Void.class) {
            return null;
        }
        if (type.isInstance(data)) {
            return data;
        }
        switch (type.getSimpleName()) {
            case "DustOptions":
                return dust(data instanceof Color ? (Color) data : Color.RED);
            case "DustTransition":
                return dustTransition(data instanceof Color ? (Color) data : Color.RED);
            case "Color":
                return Color.WHITE;
            case "ItemStack":
                return item(data);
            case "BlockData":
                return blockData(material(data));
            case "MaterialData":
                return materialData(data);
            case "Float":
                return data instanceof Number ? ((Number) data).floatValue() : 0F;
            case "Integer":
                return data instanceof Number ? ((Number) data).intValue() : 0;
            default:
                throw new IllegalArgumentException("particle " + particle + " needs data of type " + type.getName());
        }
    }

    private static Object dust(Color color) {
        return new Particle.DustOptions(color, 1F);
    }

    private static Object dustTransition(Color color) {
        return new Particle.DustTransition(color, color, 1F);
    }

    private static Object blockData(Material material) {
        return material.createBlockData();
    }

    private static Object materialData(Object data) {
        if (data instanceof EranoMaterial && ((EranoMaterial) data).parseMaterial() != null) {
            EranoMaterial material = (EranoMaterial) data;
            return new MaterialData(material.parseMaterial(), (byte) material.data());
        }
        return new MaterialData(material(data));
    }

    private static ItemStack item(Object data) {
        if (data instanceof EranoMaterial) {
            ItemStack item = ((EranoMaterial) data).parseItem();
            if (item != null) {
                return item;
            }
        }
        return new ItemStack(material(data));
    }

    private static Material material(Object data) {
        if (data instanceof Material) {
            return (Material) data;
        }
        if (data instanceof EranoMaterial && ((EranoMaterial) data).parseMaterial() != null) {
            return ((EranoMaterial) data).parseMaterial();
        }
        if (data instanceof ItemStack) {
            return ((ItemStack) data).getType();
        }
        throw new IllegalArgumentException("this particle needs a material, got " + data);
    }

    private static ServerNames<EranoParticle, Particle> server() {
        if (server == null) {
            synchronized (BukkitParticles.class) {
                if (server == null) {
                    server = new ServerNames<>(EranoParticle.class, EranoParticle.table(),
                            ServerNames.staticFields(Particle.class, Particle.class));
                }
            }
        }
        return server;
    }
}
