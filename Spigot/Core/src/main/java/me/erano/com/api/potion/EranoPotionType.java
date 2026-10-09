package me.erano.com.api.potion;

import java.util.List;
import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import me.erano.com.api.internal.NameTable;
import me.erano.com.api.internal.ServerNames;

/**
 * Every potion type of the newest Minecraft by its newest name, on every server from 1.8 to 26.x
 * ({@code SWIFTNESS} is {@code SPEED} up to 1.20.4), and its item on each: a data value on 1.8, {@code PotionData}
 * on 1.9 - 1.20.1, the type itself after. Long and strong types ({@code LONG_SWIFTNESS}) are their own constants
 * only since 1.20.2; before, their item is the base type with the flag. Generated from Spigot's data, see
 * {@code docs/Potion.md}.
 *
 * <pre>{@code
 * ItemStack potion = EranoPotionType.STRONG_HEALING.parseItem(EranoPotionType.Form.SPLASH, 1);
 * }</pre>
 */
public enum EranoPotionType {

    // <generated constants>
    AWKWARD,
    FIRE_RESISTANCE,
    HARMING,
    HEALING,
    INFESTED,
    INVISIBILITY,
    LEAPING,
    LONG_FIRE_RESISTANCE,
    LONG_INVISIBILITY,
    LONG_LEAPING,
    LONG_NIGHT_VISION,
    LONG_POISON,
    LONG_REGENERATION,
    LONG_SLOWNESS,
    LONG_SLOW_FALLING,
    LONG_STRENGTH,
    LONG_SWIFTNESS,
    LONG_TURTLE_MASTER,
    LONG_WATER_BREATHING,
    LONG_WEAKNESS,
    LUCK,
    MUNDANE,
    NIGHT_VISION,
    OOZING,
    POISON,
    REGENERATION,
    SLOWNESS,
    SLOW_FALLING,
    STRENGTH,
    STRONG_HARMING,
    STRONG_HEALING,
    STRONG_LEAPING,
    STRONG_POISON,
    STRONG_REGENERATION,
    STRONG_SLOWNESS,
    STRONG_STRENGTH,
    STRONG_SWIFTNESS,
    STRONG_TURTLE_MASTER,
    SWIFTNESS,
    THICK,
    TURTLE_MASTER,
    WATER,
    WATER_BREATHING,
    WEAKNESS,
    WEAVING,
    WIND_CHARGED;
    // </generated constants>

    /** Drunk, thrown, or thrown leaving a cloud (1.9+). */
    public enum Form {
        DRINK, SPLASH, LINGERING
    }

    private static final String LONG = "LONG_";
    private static final String STRONG = "STRONG_";

    private static volatile NameTable<EranoPotionType> table;
    private static volatile ServerNames<EranoPotionType, PotionType> server;

    /** The server's potion type, {@code null} if it doesn't have it (long / strong ones before 1.20.2: see {@link #base}). */
    public PotionType parsePotionType() {
        return server().get(this);
    }

    /** Whether {@link #parseItem} gives an item here (the type itself, or its base with the flag). */
    public boolean isSupported() {
        return parsePotionType() != null || (base() != this && base().parsePotionType() != null);
    }

    /** {@code SWIFTNESS} for {@code LONG_SWIFTNESS} and {@code STRONG_SWIFTNESS}, else itself. */
    public EranoPotionType base() {
        String name = name();
        String base = name.startsWith(LONG) ? name.substring(LONG.length())
                : name.startsWith(STRONG) ? name.substring(STRONG.length()) : null;
        if (base == null) {
            return this;
        }
        try {
            return valueOf(base);
        } catch (IllegalArgumentException e) {
            return this;
        }
    }

    /** A {@code LONG_} type: longer duration. */
    public boolean isExtended() {
        return base() != this && name().startsWith(LONG);
    }

    /** A {@code STRONG_} type: level II. */
    public boolean isUpgraded() {
        return base() != this && name().startsWith(STRONG);
    }

    /** Its Minecraft key today, e.g. {@code long_swiftness}. */
    public String key() {
        return table().key(this);
    }

    /** The names it had on older servers, newest first. */
    public List<String> oldNames() {
        return table().oldNames(this);
    }

    /** @return {@code null} if the server can't make it (the type, or lingering potions on 1.8) */
    public ItemStack parseItem(Form form, int amount) {
        return PotionItems.item(this, form, amount);
    }

    public ItemStack parseItem() {
        return parseItem(Form.DRINK, 1);
    }

    /** Today's name, an older one ({@code SPEED}, {@code INSTANT_HEAL}) or the key ({@code minecraft:swiftness}). */
    public static Optional<EranoPotionType> match(String name) {
        return table().match(name);
    }

    /** Which type the server's is, {@code null} if none. */
    public static EranoPotionType of(PotionType type) {
        return server().of(type);
    }

    static NameTable<EranoPotionType> table() {
        if (table == null) {
            synchronized (EranoPotionType.class) {
                if (table == null) {
                    table = NameTable.load(EranoPotionType.class, "/eranoapi/potion/types.tsv");
                }
            }
        }
        return table;
    }

    private static ServerNames<EranoPotionType, PotionType> server() {
        if (server == null) {
            synchronized (EranoPotionType.class) {
                if (server == null) {
                    server = new ServerNames<>(EranoPotionType.class, table(),
                            ServerNames.staticFields(PotionType.class, PotionType.class));
                }
            }
        }
        return server;
    }
}
