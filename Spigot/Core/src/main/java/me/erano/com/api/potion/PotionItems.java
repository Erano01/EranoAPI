package me.erano.com.api.potion;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

/**
 * A potion item on each version: 1.8 by its {@code Potion} class (a data value; gone from today's API, so by
 * reflection), 1.9 - 1.20.1 by {@code PotionData}, 1.20.2+ by the type itself.
 */
final class PotionItems {

    private PotionItems() {
    }

    static ItemStack item(EranoPotionType type, EranoPotionType.Form form, int amount) {
        Material splash = Material.getMaterial("SPLASH_POTION");
        if (splash == null) {
            return legacy(type, form, amount);
        }
        Material material = form == EranoPotionType.Form.DRINK ? Material.POTION
                : form == EranoPotionType.Form.SPLASH ? splash : Material.getMaterial("LINGERING_POTION");
        if (material == null) {
            return null;
        }
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        if (!(itemMeta instanceof PotionMeta)) {
            return null;
        }
        PotionMeta meta = (PotionMeta) itemMeta;
        PotionType own = type.parsePotionType();
        if (own == null || !setBaseType(meta, own)) {
            PotionType base = type.base().parsePotionType();
            if (base == null) {
                return null;
            }
            try {
                meta.setBasePotionData(new PotionData(base, type.isExtended(), type.isUpgraded()));
            } catch (IllegalArgumentException e) {
                return null; // that type can't be long / strong here
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    /** 1.20.2+; {@code false} before (no such method). */
    private static boolean setBaseType(PotionMeta meta, PotionType type) {
        try {
            meta.setBasePotionType(type);
            return true;
        } catch (NoSuchMethodError | AbstractMethodError e) {
            return false;
        }
    }

    /** 1.8: {@code new Potion(type)}, level, splash, extended, {@code toItemStack(amount)}. */
    private static ItemStack legacy(EranoPotionType type, EranoPotionType.Form form, int amount) {
        if (form == EranoPotionType.Form.LINGERING) {
            return null;
        }
        PotionType base = type.base().parsePotionType();
        if (base == null) {
            return null;
        }
        try {
            Class<?> potionClass = Class.forName("org.bukkit.potion.Potion");
            Object potion = potionClass.getConstructor(PotionType.class).newInstance(base);
            if (type.isUpgraded()) {
                potionClass.getMethod("setLevel", int.class).invoke(potion, 2);
            }
            potionClass.getMethod("setSplash", boolean.class).invoke(potion, form == EranoPotionType.Form.SPLASH);
            if (type.isExtended()) {
                potionClass.getMethod("setHasExtendedDuration", boolean.class).invoke(potion, true);
            }
            return (ItemStack) potionClass.getMethod("toItemStack", int.class).invoke(potion, amount);
        } catch (ReflectiveOperationException | RuntimeException e) {
            return null; // a type that can't be strong / long there
        }
    }
}
