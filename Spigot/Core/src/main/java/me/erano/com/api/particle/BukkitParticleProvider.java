package me.erano.com.api.particle;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.erano.com.common.VersionRange;

/**
 * 1.9+ particles through the Bukkit {@link Particle} API, no NMS. 1.20.5 renamed most constants
 * ({@code REDSTONE} → {@code DUST} ...), so each {@link ParticleEffect} is looked up by its own name
 * first and by its 1.20.5+ name second.
 */
public class BukkitParticleProvider implements IParticleProvider {

    /** {@link ParticleEffect} name → Bukkit name since 1.20.5. */
    private static final Map<String, String> RENAMED = new HashMap<>();

    static {
        RENAMED.put("EXPLOSION_NORMAL", "POOF");
        RENAMED.put("EXPLOSION_LARGE", "EXPLOSION");
        RENAMED.put("EXPLOSION_HUGE", "EXPLOSION_EMITTER");
        RENAMED.put("FIREWORKS_SPARK", "FIREWORK");
        RENAMED.put("WATER_BUBBLE", "BUBBLE");
        RENAMED.put("WATER_SPLASH", "SPLASH");
        RENAMED.put("WATER_WAKE", "FISHING");
        RENAMED.put("SUSPENDED", "UNDERWATER");
        RENAMED.put("SUSPENDED_DEPTH", "UNDERWATER");
        RENAMED.put("CRIT_MAGIC", "ENCHANTED_HIT");
        RENAMED.put("SMOKE_NORMAL", "SMOKE");
        RENAMED.put("SMOKE_LARGE", "LARGE_SMOKE");
        RENAMED.put("SPELL", "EFFECT");
        RENAMED.put("SPELL_INSTANT", "INSTANT_EFFECT");
        RENAMED.put("SPELL_MOB", "ENTITY_EFFECT");
        RENAMED.put("SPELL_MOB_AMBIENT", "ENTITY_EFFECT");
        RENAMED.put("SPELL_WITCH", "WITCH");
        RENAMED.put("DRIP_WATER", "DRIPPING_WATER");
        RENAMED.put("DRIP_LAVA", "DRIPPING_LAVA");
        RENAMED.put("VILLAGER_ANGRY", "ANGRY_VILLAGER");
        RENAMED.put("VILLAGER_HAPPY", "HAPPY_VILLAGER");
        RENAMED.put("TOWN_AURA", "MYCELIUM");
        RENAMED.put("ENCHANTMENT_TABLE", "ENCHANT");
        RENAMED.put("REDSTONE", "DUST");
        RENAMED.put("SNOWBALL", "ITEM_SNOWBALL");
        RENAMED.put("SNOW_SHOVEL", "ITEM_SNOWBALL");
        RENAMED.put("SLIME", "ITEM_SLIME");
        RENAMED.put("ITEM_CRACK", "ITEM");
        RENAMED.put("BLOCK_CRACK", "BLOCK");
        RENAMED.put("BLOCK_DUST", "BLOCK");
        RENAMED.put("WATER_DROP", "RAIN");
        RENAMED.put("MOB_APPEARANCE", "ELDER_GUARDIAN");
        RENAMED.put("TOTEM", "TOTEM_OF_UNDYING");
        // Bukkit never used this name
        RENAMED.put("BUBBLE_COLUMN_DOWN", "CURRENT_DOWN");
    }

    private final Map<ParticleEffect, Particle> particles = new HashMap<>();

    public BukkitParticleProvider() {
        Map<String, Particle> byName = new HashMap<>();
        for (Particle particle : Particle.values()) {
            byName.put(particle.name(), particle);
        }
        for (ParticleEffect effect : ParticleEffect.values()) {
            Particle particle = byName.get(effect.name());
            if (particle == null && RENAMED.containsKey(effect.name())) {
                particle = byName.get(RENAMED.get(effect.name()));
            }
            if (particle != null) {
                particles.put(effect, particle);
            }
        }
    }

    @Override
    public VersionRange supportedVersions() {
        return VersionRange.atLeast("1.9");
    }

    @Override
    public boolean isSupported(ParticleEffect effect) {
        return particles.containsKey(effect);
    }

    @Override
    public void spawnParticle(World world, Location location, ParticleEffect effect, int count,
                              double offsetX, double offsetY, double offsetZ, double speed, Object data, Player... receivers) {
        Particle particle = particles.get(effect);
        if (particle == null) {
            throw new IllegalArgumentException("Particle effect " + effect + " is not supported in this version");
        }
        Object particleData = data != null ? data : defaultData(particle);
        if (receivers != null && receivers.length > 0) {
            for (Player player : receivers) {
                player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed, particleData);
            }
        } else {
            world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed, particleData);
        }
    }

    private static Object defaultData(Particle particle) {
        Class<?> type = particle.getDataType();
        if (type == Void.class) {
            return null;
        }
        // Dust has needed DustOptions since 1.13; the class only exists from then on, so check by name.
        if (type.getSimpleName().equals("DustOptions")) {
            return new Particle.DustOptions(Color.RED, 1.0F);
        }
        throw new IllegalArgumentException("Particle " + particle + " needs data of type " + type.getName());
    }
}
