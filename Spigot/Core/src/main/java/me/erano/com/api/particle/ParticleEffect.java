package me.erano.com.api.particle;

/**
 * Minecraft'taki tüm parçacık türlerini temsil eden enum.
 * Her sürümde bulunmayan parçacıklar için version bilgisi içerir.
 */
public enum ParticleEffect {
    // 1.8'den beri var olan temel parçacıklar
    EXPLOSION_NORMAL,
    EXPLOSION_LARGE,
    EXPLOSION_HUGE,
    FIREWORKS_SPARK,
    WATER_BUBBLE,
    WATER_SPLASH,
    WATER_WAKE,
    SUSPENDED,
    SUSPENDED_DEPTH,
    CRIT,
    CRIT_MAGIC,
    SMOKE_NORMAL,
    SMOKE_LARGE,
    SPELL,
    SPELL_INSTANT,
    SPELL_MOB,
    SPELL_MOB_AMBIENT,
    SPELL_WITCH,
    DRIP_WATER,
    DRIP_LAVA,
    VILLAGER_ANGRY,
    VILLAGER_HAPPY,
    TOWN_AURA,
    NOTE,
    PORTAL,
    ENCHANTMENT_TABLE,
    FLAME,
    LAVA,
    FOOTSTEP,
    CLOUD,
    REDSTONE,
    SNOWBALL,
    SNOW_SHOVEL,
    SLIME,
    HEART,
    ITEM_CRACK,
    BLOCK_CRACK,
    BLOCK_DUST,

    // 1.9
    BARRIER("1.9"),
    WATER_DROP("1.9"),
    MOB_APPEARANCE("1.9"),
    DRAGON_BREATH("1.9"),
    END_ROD("1.9"),
    DAMAGE_INDICATOR("1.9"),
    SWEEP_ATTACK("1.9"),

    // 1.10
    FALLING_DUST("1.10"),

    // 1.11
    TOTEM("1.11"),
    SPIT("1.11"),

    // 1.13
    BUBBLE_POP("1.13"),
    BUBBLE_COLUMN_UP("1.13"),
    BUBBLE_COLUMN_DOWN("1.13"),
    NAUTILUS("1.13"),
    DOLPHIN("1.13"),

    // 1.14
    SNEEZE("1.14"),
    CAMPFIRE_COSY_SMOKE("1.14"),
    CAMPFIRE_SIGNAL_SMOKE("1.14"),
    COMPOSTER("1.14"),

    // 1.15
    DRIPPING_HONEY("1.15"),
    FALLING_HONEY("1.15"),
    LANDING_HONEY("1.15"),
    FALLING_NECTAR("1.15"),

    // 1.16
    SOUL_FIRE_FLAME("1.16"),
    ASH("1.16"),
    CRIMSON_SPORE("1.16"),
    WARPED_SPORE("1.16"),
    SOUL("1.16"),
    DRIPPING_OBSIDIAN_TEAR("1.16"),
    FALLING_OBSIDIAN_TEAR("1.16"),
    LANDING_OBSIDIAN_TEAR("1.16"),
    REVERSE_PORTAL("1.16"),
    WHITE_ASH("1.16"),

    // 1.17
    LIGHT("1.17"),
    FALLING_SPORE_BLOSSOM("1.17"),
    SPORE_BLOSSOM_AIR("1.17"),
    SMALL_FLAME("1.17"),
    SNOWFLAKE("1.17"),
    DRIPPING_DRIPSTONE_LAVA("1.17"),
    FALLING_DRIPSTONE_LAVA("1.17"),
    DRIPPING_DRIPSTONE_WATER("1.17"),
    FALLING_DRIPSTONE_WATER("1.17"),
    GLOW_SQUID_INK("1.17"),
    GLOW("1.17"),
    WAX_ON("1.17"),
    WAX_OFF("1.17"),
    ELECTRIC_SPARK("1.17"),
    SCRAPE("1.17"),

    // 1.18
    BLOCK_MARKER("1.18"),

    // 1.19
    SONIC_BOOM("1.19"),
    SCULK_SOUL("1.19"),
    SCULK_CHARGE("1.19"),
    SCULK_CHARGE_POP("1.19"),
    SHRIEK("1.19"),

    // 1.20
    CHERRY_LEAVES("1.20"),
    EGG_CRACK("1.20"),
    DUST_PLUME("1.20"),
    WHITE_SMOKE("1.20"),
    TRIAL_SPAWNER_DETECTION("1.20.5");

    private final String minVersion;

    ParticleEffect() {
        this.minVersion = "1.8"; // Default minimum version
    }

    ParticleEffect(String minVersion) {
        this.minVersion = minVersion;
    }

    /**
     * Parçacığın desteklendiği minimum Minecraft sürümünü döndürür
     */
    public String getMinVersion() {
        return minVersion;
    }
}
