package me.erano.com.api.particle;

import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import me.erano.com.api.internal.NameTable;

/**
 * Every particle of the newest Minecraft by its newest name, on every server from 1.8 to 26.x: 1.20.5 renamed most
 * of them ({@code DUST} was {@code REDSTONE}), and 1.8 has no particle API at all (EranoAPI sends its packet).
 * Generated from Spigot's data, see {@code docs/Particle.md}.
 *
 * <p>Data, when the particle takes some, can be given the same way on every version and is converted: a
 * {@code Color} for {@code DUST} (red if none) and {@code ENTITY_EFFECT}; a {@code Material}, an
 * {@code EranoMaterial} or an {@code ItemStack} for {@code BLOCK}, {@code FALLING_DUST}, {@code ITEM} ...; or the
 * server's own type ({@code BlockData}, {@code DustOptions} ...).
 *
 * <pre>{@code
 * EranoParticle.HAPPY_VILLAGER.spawn(location, 10, 0.5, 0.5, 0.5, 0);
 * EranoParticle.DUST.spawn(location, 1, 0, 0, 0, 0, Color.AQUA);
 * EranoParticle.BLOCK.spawn(location, 30, 0.3, 0.3, 0.3, 0, EranoMaterial.RED_WOOL);
 * }</pre>
 */
public enum EranoParticle {

    // <generated constants>
    ANGRY_VILLAGER,
    ASH,
    BLOCK,
    BLOCK_CRUMBLE,
    BLOCK_MARKER,
    BUBBLE,
    BUBBLE_COLUMN_UP,
    BUBBLE_POP,
    CAMPFIRE_COSY_SMOKE,
    CAMPFIRE_SIGNAL_SMOKE,
    CHERRY_LEAVES,
    CLOUD,
    COMPOSTER,
    COPPER_FIRE_FLAME,
    CRIMSON_SPORE,
    CRIT,
    CURRENT_DOWN,
    DAMAGE_INDICATOR,
    DOLPHIN,
    DRAGON_BREATH,
    DRIPPING_DRIPSTONE_LAVA,
    DRIPPING_DRIPSTONE_WATER,
    DRIPPING_HONEY,
    DRIPPING_LAVA,
    DRIPPING_OBSIDIAN_TEAR,
    DRIPPING_WATER,
    DUST,
    DUST_COLOR_TRANSITION,
    DUST_PILLAR,
    DUST_PLUME,
    EFFECT,
    EGG_CRACK,
    ELDER_GUARDIAN,
    ELECTRIC_SPARK,
    ENCHANT,
    ENCHANTED_HIT,
    END_ROD,
    ENTITY_EFFECT,
    EXPLOSION,
    EXPLOSION_EMITTER,
    FALLING_DRIPSTONE_LAVA,
    FALLING_DRIPSTONE_WATER,
    FALLING_DUST,
    FALLING_HONEY,
    FALLING_LAVA,
    FALLING_NECTAR,
    FALLING_OBSIDIAN_TEAR,
    FALLING_SPORE_BLOSSOM,
    FALLING_WATER,
    FIREFLY,
    FIREWORK,
    FISHING,
    FLAME,
    FLASH,
    GEYSER,
    GEYSER_BASE,
    GEYSER_PLUME,
    GEYSER_POOF,
    GLOW,
    GLOW_SQUID_INK,
    GUST,
    GUST_EMITTER_LARGE,
    GUST_EMITTER_SMALL,
    HAPPY_VILLAGER,
    HEART,
    INFESTED,
    INSTANT_EFFECT,
    ITEM,
    ITEM_COBWEB,
    ITEM_SLIME,
    ITEM_SNOWBALL,
    LANDING_HONEY,
    LANDING_LAVA,
    LANDING_OBSIDIAN_TEAR,
    LARGE_SMOKE,
    LAVA,
    MYCELIUM,
    NAUTILUS,
    NOTE,
    NOXIOUS_GAS,
    NOXIOUS_GAS_CLOUD,
    OMINOUS_SPAWNING,
    ORANGE_POPLAR_LEAVES,
    PALE_OAK_LEAVES,
    PAUSE_MOB_GROWTH,
    POOF,
    PORTAL,
    RAID_OMEN,
    RAIN,
    RED_POPLAR_LEAVES,
    RESET_MOB_GROWTH,
    REVERSE_PORTAL,
    SCRAPE,
    SCULK_CHARGE,
    SCULK_CHARGE_POP,
    SCULK_SOUL,
    SHRIEK,
    SMALL_FLAME,
    SMALL_GUST,
    SMOKE,
    SNEEZE,
    SNOWFLAKE,
    SONIC_BOOM,
    SOUL,
    SOUL_FIRE_FLAME,
    SPIT,
    SPLASH,
    SPORE_BLOSSOM_AIR,
    SQUID_INK,
    SULFUR_BUBBLES,
    SULFUR_CUBE_GOO,
    SWEEP_ATTACK,
    TINTED_LEAVES,
    TOTEM_OF_UNDYING,
    TRAIL,
    TRIAL_OMEN,
    TRIAL_SPAWNER_DETECTION,
    TRIAL_SPAWNER_DETECTION_OMINOUS,
    UNDERWATER,
    VAULT_CONNECTION,
    VIBRATION,
    WARPED_SPORE,
    WAX_OFF,
    WAX_ON,
    WHITE_ASH,
    WHITE_SMOKE,
    WITCH,
    YELLOW_POPLAR_LEAVES;
    // </generated constants>

    private static volatile NameTable<EranoParticle> table;
    private static volatile Boolean legacy;

    /** The server's particle, {@code null} if it doesn't have it or is 1.8 (no {@code Particle} there). */
    public Particle parseParticle() {
        return legacy() ? null : BukkitParticles.get(this);
    }

    public boolean isSupported() {
        return legacy() ? LegacyParticles.get(this) != null : BukkitParticles.get(this) != null;
    }

    /** Its Minecraft key today, e.g. {@code happy_villager}. */
    public String key() {
        return table().key(this);
    }

    /** The names it had on older servers, newest first. */
    public List<String> oldNames() {
        return table().oldNames(this);
    }

    public void spawn(Location location, int count, double offsetX, double offsetY, double offsetZ, double speed) {
        spawn(location, count, offsetX, offsetY, offsetZ, speed, null);
    }

    /**
     * To the players near the location, or only to {@code receivers} when given. Nothing if the server doesn't have
     * it.
     *
     * @param data see the class comment; {@code null} for none
     * @throws IllegalArgumentException if it needs data that can't be made from {@code data}
     */
    public void spawn(Location location, int count, double offsetX, double offsetY, double offsetZ, double speed,
                      Object data, Player... receivers) {
        if (legacy()) {
            LegacyParticles.spawn(this, location, count, offsetX, offsetY, offsetZ, speed, data, receivers);
        } else {
            BukkitParticles.spawn(this, location, count, offsetX, offsetY, offsetZ, speed, data, receivers);
        }
    }

    /** Today's name, an older one ({@code VILLAGER_HAPPY}) or the key ({@code minecraft:happy_villager}). */
    public static Optional<EranoParticle> match(String name) {
        return table().match(name);
    }

    /** Which particle the server's is, {@code null} if none. */
    public static EranoParticle of(Particle particle) {
        return BukkitParticles.of(particle);
    }

    static NameTable<EranoParticle> table() {
        if (table == null) {
            synchronized (EranoParticle.class) {
                if (table == null) {
                    table = NameTable.load(EranoParticle.class, "/eranoapi/particle/particles.tsv");
                }
            }
        }
        return table;
    }

    /** 1.8: no {@code org.bukkit.Particle}. */
    static boolean legacy() {
        if (legacy == null) {
            boolean missing;
            try {
                Class.forName("org.bukkit.Particle", false, EranoParticle.class.getClassLoader());
                missing = false;
            } catch (ClassNotFoundException e) {
                missing = true;
            }
            legacy = missing;
        }
        return legacy;
    }
}
