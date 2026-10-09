package me.erano.com.api.material;

import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/** {@link EranoMaterial}s on one server: its {@link Material} for each, and data values on 1.8 - 1.12. */
final class ServerMaterials {

    private final MaterialTable table;
    private final boolean legacy;
    private final Map<EranoMaterial, Material> materials = new EnumMap<>(EranoMaterial.class);
    private final Map<EranoMaterial, Short> data = new EnumMap<>(EranoMaterial.class);
    /** 1.13+: the server's material -> ours. */
    private final Map<Material, EranoMaterial> byMaterial = new HashMap<>();
    /** 1.8 - 1.12: Block#setTypeIdAndData, gone from newer APIs. */
    private Method setTypeIdAndData;

    /**
     * @param legacy a 1.8 - 1.12 server
     * @param lookup the server's material of a name ({@code Material::getMaterial}), {@code null} for none
     */
    ServerMaterials(MaterialTable table, boolean legacy, Function<String, Material> lookup) {
        this.table = table;
        this.legacy = legacy;
        for (EranoMaterial material : EranoMaterial.values()) {
            Material found = null;
            if (legacy) {
                String name = table.legacyName(material);
                if (!name.isEmpty()) {
                    found = lookup.apply(name);
                    if (found != null) {
                        data.put(material, table.legacyData(material));
                    }
                }
            } else {
                found = lookup.apply(material.name());
                for (String old : table.oldNames(material)) {
                    if (found != null) {
                        break;
                    }
                    found = lookup.apply(old);
                }
            }
            if (found != null) {
                materials.put(material, found);
                if (!legacy && !byMaterial.containsKey(found)) {
                    byMaterial.put(found, material);
                }
            }
        }
    }

    Material material(EranoMaterial material) {
        return materials.get(material);
    }

    short data(EranoMaterial material) {
        Short value = data.get(material);
        return value == null ? 0 : value;
    }

    @SuppressWarnings("deprecation")
    Optional<EranoMaterial> ofItem(ItemStack item) {
        if (legacy) {
            return Optional.ofNullable(table.legacyItem(item.getType().name(), item.getDurability()));
        }
        return Optional.ofNullable(byMaterial.get(item.getType()));
    }

    @SuppressWarnings("deprecation")
    Optional<EranoMaterial> ofBlock(Block block) {
        if (legacy) {
            return Optional.ofNullable(table.legacyBlock(block.getType().name(), block.getData()));
        }
        return Optional.ofNullable(byMaterial.get(block.getType()));
    }

    Optional<EranoMaterial> ofMaterial(Material material) {
        if (legacy) {
            EranoMaterial item = table.legacyItem(material.name(), 0);
            return Optional.ofNullable(item != null ? item : table.legacyBlock(material.name(), 0));
        }
        return Optional.ofNullable(byMaterial.get(material));
    }

    @SuppressWarnings("deprecation")
    boolean setType(EranoMaterial what, Block block, boolean physics) {
        Material material = material(what);
        if (material == null) {
            return false;
        }
        short value = data(what);
        if (!legacy || value == 0) {
            block.setType(material, physics);
            return true;
        }
        try {
            if (setTypeIdAndData == null) {
                setTypeIdAndData = Block.class.getMethod("setTypeIdAndData", int.class, byte.class, boolean.class);
            }
            setTypeIdAndData.invoke(block, material.getId(), (byte) value, physics);
            return true;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Block#setTypeIdAndData on a 1.8 - 1.12 server", e);
        }
    }
}
