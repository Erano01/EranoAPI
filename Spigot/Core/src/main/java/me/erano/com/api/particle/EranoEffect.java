package me.erano.com.api.particle;

import java.util.Optional;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.erano.com.api.internal.NameTable;
import me.erano.com.api.internal.ServerNames;

/**
 * Every world effect ({@link Effect}: a sound or a sight such as a door's sound or {@code STEP_SOUND}'s block break)
 * of the newest Minecraft, on every server from 1.8 to 26.x that has it. Names never changed, effects were only
 * added; this says which server has which. Particles are {@link EranoParticle}. See {@code docs/Particle.md}.
 */
public enum EranoEffect {

    // <generated constants>
    ANVIL_BREAK,
    ANVIL_LAND,
    ANVIL_USE,
    BAT_TAKEOFF,
    BLAZE_SHOOT,
    BONE_MEAL_USE,
    BOOK_PAGE_TURN,
    BOW_FIRE,
    BREWING_STAND_BREW,
    CHORUS_FLOWER_DEATH,
    CHORUS_FLOWER_GROW,
    CLICK1,
    CLICK2,
    COMPOSTER_FILL_ATTEMPT,
    COPPER_WAX_OFF,
    COPPER_WAX_ON,
    DOOR_CLOSE,
    DOOR_TOGGLE,
    DRAGON_BREATH,
    DRIPPING_DRIPSTONE,
    ELECTRIC_SPARK,
    ENDERDRAGON_GROWL,
    ENDERDRAGON_SHOOT,
    ENDEREYE_LAUNCH,
    ENDER_DRAGON_DESTROY_BLOCK,
    ENDER_SIGNAL,
    END_GATEWAY_SPAWN,
    END_PORTAL_FRAME_FILL,
    EXTINGUISH,
    FENCE_GATE_CLOSE,
    FENCE_GATE_TOGGLE,
    FIREWORK_SHOOT,
    GHAST_SHOOT,
    GHAST_SHRIEK,
    GRINDSTONE_USE,
    HUSK_CONVERTED_TO_ZOMBIE,
    INSTANT_POTION_BREAK,
    IRON_DOOR_CLOSE,
    IRON_DOOR_TOGGLE,
    IRON_TRAPDOOR_CLOSE,
    IRON_TRAPDOOR_TOGGLE,
    LAVA_INTERACT,
    MOBSPAWNER_FLAMES,
    OXIDISED_COPPER_SCRAPE,
    PHANTOM_BITE,
    POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON,
    POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON,
    POINTED_DRIPSTONE_LAND,
    PORTAL_TRAVEL,
    POTION_BREAK,
    RECORD_PLAY,
    REDSTONE_TORCH_BURNOUT,
    SKELETON_CONVERTED_TO_STRAY,
    SMITHING_TABLE_USE,
    SMOKE,
    SPONGE_DRY,
    STEP_SOUND,
    TRAPDOOR_CLOSE,
    TRAPDOOR_TOGGLE,
    VILLAGER_PLANT_GROW,
    WITHER_BREAK_BLOCK,
    WITHER_SHOOT,
    ZOMBIE_CHEW_IRON_DOOR,
    ZOMBIE_CHEW_WOODEN_DOOR,
    ZOMBIE_CONVERTED_TO_DROWNED,
    ZOMBIE_CONVERTED_VILLAGER,
    ZOMBIE_DESTROY_DOOR,
    ZOMBIE_INFECT;
    // </generated constants>

    private static volatile NameTable<EranoEffect> table;
    private static volatile ServerNames<EranoEffect, Effect> server;

    /** The server's effect, {@code null} if it doesn't have it. */
    public Effect parseEffect() {
        return server().get(this);
    }

    public boolean isSupported() {
        return parseEffect() != null;
    }

    /** The first NMS revision that has it, e.g. {@code V1_9_R1}. */
    public String since() {
        return table().first(this);
    }

    /** To the players near the location; for effects without data. */
    public void play(Location location) {
        Effect effect = parseEffect();
        if (effect != null && location.getWorld() != null) {
            location.getWorld().playEffect(location, effect, 0);
        }
    }

    /** @param data what the effect takes ({@code Effect#getData}), e.g. a {@code Material} for {@code STEP_SOUND} */
    public void play(Location location, Object data) {
        Effect effect = parseEffect();
        if (effect != null && location.getWorld() != null) {
            location.getWorld().playEffect(location, effect, data);
        }
    }

    /** To this player only. */
    public void play(Player player, Location location, Object data) {
        Effect effect = parseEffect();
        if (effect != null) {
            player.playEffect(location, effect, data);
        }
    }

    public static Optional<EranoEffect> match(String name) {
        return table().match(name);
    }

    public static EranoEffect of(Effect effect) {
        return server().of(effect);
    }

    static NameTable<EranoEffect> table() {
        if (table == null) {
            synchronized (EranoEffect.class) {
                if (table == null) {
                    table = NameTable.load(EranoEffect.class, "/eranoapi/particle/effects.tsv");
                }
            }
        }
        return table;
    }

    private static ServerNames<EranoEffect, Effect> server() {
        if (server == null) {
            synchronized (EranoEffect.class) {
                if (server == null) {
                    server = new ServerNames<>(EranoEffect.class, table(), ServerNames.staticFields(Effect.class, Effect.class));
                }
            }
        }
        return server;
    }
}
