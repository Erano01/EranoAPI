package me.erano.com.api.potion;

import java.util.List;
import java.util.Optional;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.erano.com.api.internal.NameTable;
import me.erano.com.api.internal.ServerNames;

/**
 * Every potion effect of the newest Minecraft by its newest name, on every server from 1.8 to 26.x
 * ({@code STRENGTH} is {@code INCREASE_DAMAGE} up to 1.20.4). Generated from Spigot's data, see
 * {@code docs/Potion.md}.
 *
 * <pre>{@code
 * EranoPotionEffect.SPEED.apply(player, 200, 1); // Speed II, 10 s
 * }</pre>
 */
public enum EranoPotionEffect {

    // <generated constants>
    ABSORPTION,
    BAD_OMEN,
    BLINDNESS,
    BREATH_OF_THE_NAUTILUS,
    CONDUIT_POWER,
    DARKNESS,
    DOLPHINS_GRACE,
    FIRE_RESISTANCE,
    GLOWING,
    HASTE,
    HEALTH_BOOST,
    HERO_OF_THE_VILLAGE,
    HUNGER,
    INFESTED,
    INSTANT_DAMAGE,
    INSTANT_HEALTH,
    INVISIBILITY,
    JUMP_BOOST,
    LEVITATION,
    LUCK,
    MINING_FATIGUE,
    NAUSEA,
    NIGHT_VISION,
    OOZING,
    POISON,
    RAID_OMEN,
    REGENERATION,
    RESISTANCE,
    SATURATION,
    SLOWNESS,
    SLOW_FALLING,
    SPEED,
    STRENGTH,
    TRIAL_OMEN,
    UNLUCK,
    WATER_BREATHING,
    WEAKNESS,
    WEAVING,
    WIND_CHARGED,
    WITHER;
    // </generated constants>

    private static volatile NameTable<EranoPotionEffect> table;
    private static volatile ServerNames<EranoPotionEffect, PotionEffectType> server;

    /** The server's effect type, {@code null} if it doesn't have it. */
    public PotionEffectType parsePotionEffectType() {
        return server().get(this);
    }

    public boolean isSupported() {
        return parsePotionEffectType() != null;
    }

    /** Its Minecraft key today, e.g. {@code strength}. */
    public String key() {
        return table().key(this);
    }

    /** Its numeric id (never changed, 1 = speed). */
    public int id() {
        String id = table().extra(this);
        return id.isEmpty() ? -1 : Integer.parseInt(id);
    }

    /** The names it had on older servers, newest first. */
    public List<String> oldNames() {
        return table().oldNames(this);
    }

    /**
     * @param ticks     20 = 1 s
     * @param amplifier 0 = level I
     * @return {@code null} if the server doesn't have it
     */
    public PotionEffect buildPotionEffect(int ticks, int amplifier) {
        PotionEffectType type = parsePotionEffectType();
        return type == null ? null : new PotionEffect(type, ticks, amplifier);
    }

    /** @return {@code false} if the server doesn't have it */
    public boolean apply(LivingEntity entity, int ticks, int amplifier) {
        PotionEffect effect = buildPotionEffect(ticks, amplifier);
        return effect != null && entity.addPotionEffect(effect);
    }

    public void remove(LivingEntity entity) {
        PotionEffectType type = parsePotionEffectType();
        if (type != null) {
            entity.removePotionEffect(type);
        }
    }

    /** Today's name, an older one ({@code INCREASE_DAMAGE}) or the key ({@code minecraft:strength}). */
    public static Optional<EranoPotionEffect> match(String name) {
        return table().match(name);
    }

    /** Which effect the server's type is, {@code null} if none. */
    public static EranoPotionEffect of(PotionEffectType type) {
        return server().of(type);
    }

    static NameTable<EranoPotionEffect> table() {
        if (table == null) {
            synchronized (EranoPotionEffect.class) {
                if (table == null) {
                    table = NameTable.load(EranoPotionEffect.class, "/eranoapi/potion/effects.tsv");
                }
            }
        }
        return table;
    }

    private static ServerNames<EranoPotionEffect, PotionEffectType> server() {
        if (server == null) {
            synchronized (EranoPotionEffect.class) {
                if (server == null) {
                    server = new ServerNames<>(EranoPotionEffect.class, table(),
                            ServerNames.staticFields(PotionEffectType.class, PotionEffectType.class));
                }
            }
        }
        return server;
    }
}
