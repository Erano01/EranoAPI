package me.erano.com.api.particle;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.erano.com.api.internal.ServerNames;
import me.erano.com.api.material.EranoMaterial;

/**
 * 1.8 (R1 - R3): no particle API, so the packet NMS sends ({@code PacketPlayOutWorldParticles} with an
 * {@code EnumParticle}), by reflection: the three revisions differ only in their package. 1.9's {@code Particle} took
 * {@code EnumParticle}'s names, so a particle's name here is its 1.9 - 1.20.4 name.
 */
final class LegacyParticles {

    /** What the client draws particles within, like Bukkit 1.9+. */
    private static final double RANGE = 32;

    private static volatile ServerNames<EranoParticle, Object> server;
    private static Constructor<?> packet;
    private static Method getHandle;
    private static Field connection;
    private static Method sendPacket;

    private LegacyParticles() {
    }

    static Object get(EranoParticle particle) {
        return server().get(particle);
    }

    static void spawn(EranoParticle effect, Location location, int count, double offsetX, double offsetY,
                      double offsetZ, double speed, Object data, Player... receivers) {
        Object particle = get(effect);
        if (particle == null || location.getWorld() == null) {
            return;
        }
        String name = ((Enum<?>) particle).name();
        float x = (float) offsetX;
        float y = (float) offsetY;
        float z = (float) offsetZ;
        float extra = (float) speed;
        int amount = count;
        if (data instanceof Color && (name.equals("REDSTONE") || name.equals("SPELL_MOB") || name.equals("SPELL_MOB_AMBIENT"))) {
            // A colored particle is one particle whose offsets are the color (0 means default, so not quite 0)
            Color color = (Color) data;
            x = Math.max(color.getRed() / 255F, Float.MIN_NORMAL);
            y = color.getGreen() / 255F;
            z = color.getBlue() / 255F;
            extra = 1;
            amount = 0;
        }
        int[] extras = extras(name, data);
        try {
            Object message = packet().newInstance(particle, false, (float) location.getX(), (float) location.getY(),
                    (float) location.getZ(), x, y, z, extra, amount, extras);
            for (Player player : receivers(location, receivers)) {
                Object handle = getHandle.invoke(player);
                sendPacket.invoke(connection.get(handle), message);
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("can't send 1.8's particle packet", e);
        }
    }

    /** Item: {@code {id, data}}; block: {@code {id | data << 12}}. */
    @SuppressWarnings("deprecation")
    private static int[] extras(String name, Object data) {
        boolean item = name.equals("ITEM_CRACK");
        boolean block = name.equals("BLOCK_CRACK") || name.equals("BLOCK_DUST");
        if (!item && !block) {
            return new int[0];
        }
        int id;
        int value;
        if (data instanceof EranoMaterial && ((EranoMaterial) data).parseMaterial() != null) {
            id = ((EranoMaterial) data).parseMaterial().getId();
            value = ((EranoMaterial) data).data();
        } else if (data instanceof ItemStack) {
            id = ((ItemStack) data).getType().getId();
            value = ((ItemStack) data).getDurability();
        } else if (data instanceof Material) {
            id = ((Material) data).getId();
            value = 0;
        } else {
            throw new IllegalArgumentException("particle " + name + " needs a material, got " + data);
        }
        return item ? new int[] {id, value} : new int[] {id | value << 12};
    }

    private static Collection<? extends Player> receivers(Location location, Player[] receivers) {
        if (receivers != null && receivers.length > 0) {
            List<Player> list = new ArrayList<>();
            for (Player player : receivers) {
                list.add(player);
            }
            return list;
        }
        List<Player> near = new ArrayList<>();
        for (Player player : location.getWorld().getPlayers()) {
            if (player.getLocation().distanceSquared(location) <= RANGE * RANGE) {
                near.add(player);
            }
        }
        return near;
    }

    private static synchronized Constructor<?> packet() throws ReflectiveOperationException {
        if (packet == null) {
            String nms = "net.minecraft.server." + revision() + ".";
            Class<?> enumParticle = Class.forName(nms + "EnumParticle");
            Class<?> packetClass = Class.forName(nms + "PacketPlayOutWorldParticles");
            packet = packetClass.getConstructor(enumParticle, boolean.class, float.class, float.class, float.class,
                    float.class, float.class, float.class, float.class, int.class, int[].class);
            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + revision() + ".entity.CraftPlayer");
            getHandle = craftPlayer.getMethod("getHandle");
            connection = getHandle.getReturnType().getField("playerConnection");
            sendPacket = connection.getType().getMethod("sendPacket", Class.forName(nms + "Packet"));
        }
        return packet;
    }

    /** {@code v1_8_R3} from {@code org.bukkit.craftbukkit.v1_8_R3.CraftServer}. */
    private static String revision() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static ServerNames<EranoParticle, Object> server() {
        if (server == null) {
            synchronized (LegacyParticles.class) {
                if (server == null) {
                    Class<? extends Enum> enumParticle;
                    try {
                        enumParticle = (Class<? extends Enum>) Class.forName("net.minecraft.server." + revision() + ".EnumParticle");
                    } catch (ClassNotFoundException e) {
                        throw new IllegalStateException("no particle API and no 1.8 EnumParticle on this server", e);
                    }
                    Class<? extends Enum> type = enumParticle;
                    server = new ServerNames<>(EranoParticle.class, EranoParticle.table(), name -> {
                        try {
                            return Enum.valueOf(type, name);
                        } catch (IllegalArgumentException e) {
                            return null;
                        }
                    });
                }
            }
        }
        return server;
    }
}
