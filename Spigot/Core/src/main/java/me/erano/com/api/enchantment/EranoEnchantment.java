package me.erano.com.api.enchantment;

import java.util.List;
import java.util.Optional;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import me.erano.com.api.internal.NameTable;
import me.erano.com.api.internal.ServerNames;

/**
 * Every enchantment of the newest Minecraft by its newest name, on every server from 1.8 to 26.x
 * ({@code SHARPNESS} is {@code DAMAGE_ALL} up to 1.20.4). Generated from Spigot's data, see
 * {@code docs/Enchantment.md}.
 *
 * <pre>{@code
 * EranoEnchantment.SHARPNESS.enchant(sword, 2);
 * EranoEnchantment.match("DAMAGE_ALL"); // SHARPNESS
 * }</pre>
 */
public enum EranoEnchantment {

    // <generated constants>
    AQUA_AFFINITY,
    BANE_OF_ARTHROPODS,
    BINDING_CURSE,
    BLAST_PROTECTION,
    BREACH,
    CHANNELING,
    DENSITY,
    DEPTH_STRIDER,
    EFFICIENCY,
    FEATHER_FALLING,
    FIRE_ASPECT,
    FIRE_PROTECTION,
    FLAME,
    FORTUNE,
    FROST_WALKER,
    IMPALING,
    INFINITY,
    KNOCKBACK,
    LOOTING,
    LOYALTY,
    LUCK_OF_THE_SEA,
    LUNGE,
    LURE,
    MENDING,
    MULTISHOT,
    PIERCING,
    POWER,
    PROJECTILE_PROTECTION,
    PROTECTION,
    PUNCH,
    QUICK_CHARGE,
    RESPIRATION,
    RIPTIDE,
    SHARPNESS,
    SILK_TOUCH,
    SMITE,
    SOUL_SPEED,
    SWEEPING_EDGE,
    SWIFT_SNEAK,
    THORNS,
    UNBREAKING,
    VANISHING_CURSE,
    WIND_BURST;
    // </generated constants>

    private static volatile NameTable<EranoEnchantment> table;
    private static volatile ServerNames<EranoEnchantment, Enchantment> server;

    /** The server's enchantment, {@code null} if it doesn't have it. */
    public Enchantment parseEnchantment() {
        return server().get(this);
    }

    public boolean isSupported() {
        return parseEnchantment() != null;
    }

    /** Its Minecraft key today, e.g. {@code sharpness}. */
    public String key() {
        return table().key(this);
    }

    /** Its numeric id on 1.8 - 1.12, {@code -1} if it came later. */
    public int legacyId() {
        String id = table().extra(this);
        return id.isEmpty() ? -1 : Integer.parseInt(id);
    }

    /** The names it had on older servers, newest first. */
    public List<String> oldNames() {
        return table().oldNames(this);
    }

    /**
     * Puts it on the item at any level, whatever the item.
     *
     * @return {@code false} if the server doesn't have it
     */
    public boolean enchant(ItemStack item, int level) {
        Enchantment enchantment = parseEnchantment();
        if (enchantment == null) {
            return false;
        }
        item.addUnsafeEnchantment(enchantment, level);
        return true;
    }

    /** Its level on the item, 0 if none. */
    public int level(ItemStack item) {
        Enchantment enchantment = parseEnchantment();
        return enchantment == null || item == null ? 0 : item.getEnchantmentLevel(enchantment);
    }

    /** Today's name, an older one ({@code DAMAGE_ALL}) or the key ({@code minecraft:sharpness}). */
    public static Optional<EranoEnchantment> match(String name) {
        return table().match(name);
    }

    /** Which enchantment the server's is, {@code null} if none. */
    public static EranoEnchantment of(Enchantment enchantment) {
        return server().of(enchantment);
    }

    static NameTable<EranoEnchantment> table() {
        if (table == null) {
            synchronized (EranoEnchantment.class) {
                if (table == null) {
                    table = NameTable.load(EranoEnchantment.class, "/eranoapi/enchantment/enchantments.tsv");
                }
            }
        }
        return table;
    }

    private static ServerNames<EranoEnchantment, Enchantment> server() {
        if (server == null) {
            synchronized (EranoEnchantment.class) {
                if (server == null) {
                    // Static fields: on every version (getByKey only since 1.13, getByName rerouted since 1.20.5)
                    server = new ServerNames<>(EranoEnchantment.class, table(),
                            ServerNames.staticFields(Enchantment.class, Enchantment.class));
                }
            }
        }
        return server;
    }
}
