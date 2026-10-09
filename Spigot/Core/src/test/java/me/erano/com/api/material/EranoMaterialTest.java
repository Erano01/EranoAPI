package me.erano.com.api.material;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Optional;

import org.bukkit.Material;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** Against the newest spigot-api (Core's): as on a 26.x server, an older 1.13+ one and a 1.8 - 1.12 one. */
class EranoMaterialTest {

    private final MaterialTable table = new MaterialTable();

    @AfterEach
    void forgetServer() {
        MaterialTables.useServer(null);
    }

    private void onNewestServer() {
        MaterialTables.useServer(new ServerMaterials(table, false, Material::getMaterial));
    }

    /** 1.8 - 1.12: their names are today's LEGACY_ ones. */
    @SuppressWarnings("deprecation")
    private void onLegacyServer() {
        MaterialTables.useServer(new ServerMaterials(table, true, name -> Material.getMaterial("LEGACY_" + name)));
    }

    @Test
    void everyMaterialOfTheNewestServerIsAConstantAndViceVersa() {
        for (Material material : Material.values()) {
            @SuppressWarnings("deprecation")
            boolean legacy = material.isLegacy();
            if (!legacy) {
                assertTrue(MaterialTable.constant(material.name()) != null, material.name());
            }
        }
        onNewestServer();
        for (EranoMaterial material : EranoMaterial.values()) {
            assertTrue(material.isSupported(), material.name());
            assertEquals(material.name(), material.parseMaterial().name());
            assertEquals(0, material.data());
        }
    }

    @Test
    void namesInEveryFormAndAlternatives() {
        onNewestServer();
        assertEquals(Optional.of(EranoMaterial.RED_WOOL), EranoMaterial.match("RED_WOOL"));
        assertEquals(Optional.of(EranoMaterial.RED_WOOL), EranoMaterial.match("minecraft:red_wool"));
        assertEquals(Optional.of(EranoMaterial.RED_WOOL), EranoMaterial.match(" red-wool "));
        assertEquals(Optional.of(EranoMaterial.RED_WOOL), EranoMaterial.match("WOOL:14"));
        assertEquals(Optional.of(EranoMaterial.WHITE_WOOL), EranoMaterial.match("WOOL"));
        assertEquals(Optional.of(EranoMaterial.SHORT_GRASS), EranoMaterial.match("GRASS"));
        assertEquals(Optional.of(EranoMaterial.IRON_CHAIN), EranoMaterial.match("CHAIN"));
        assertEquals(Optional.of(EranoMaterial.WOODEN_SWORD), EranoMaterial.match("WOODEN_SWORD|WOOD_SWORD"));
        assertEquals(Optional.of(EranoMaterial.WOODEN_SWORD), EranoMaterial.match("WOOD_SWORD"));
        assertEquals(Optional.of(EranoMaterial.PLAYER_HEAD), EranoMaterial.match("SKULL_ITEM:3"));
        assertEquals(Optional.of(EranoMaterial.ZOMBIE_SPAWN_EGG), EranoMaterial.match("MONSTER_EGG:54"));
        assertEquals(Optional.of(EranoMaterial.GRANITE), EranoMaterial.match("STONE:1"));
        assertFalse(EranoMaterial.match("NOT_A_MATERIAL").isPresent());
        assertFalse(EranoMaterial.match("WOOL:x").isPresent());
        assertEquals(Arrays.asList("GRASS"), EranoMaterial.SHORT_GRASS.oldNames());
    }

    @Test
    void onAnOlderModernServerARenamedMaterialIsItsOldName() {
        // 1.20.2: no SHORT_GRASS yet, its material is called GRASS (played by STONE here).
        MaterialTables.useServer(new ServerMaterials(table, false,
                name -> name.equals("SHORT_GRASS") ? null : name.equals("GRASS") ? Material.STONE : Material.getMaterial(name)));
        assertEquals(Material.STONE, EranoMaterial.SHORT_GRASS.parseMaterial());
    }

    @Test
    @SuppressWarnings("deprecation")
    void onALegacyServerMaterialsAreTheOldNameWithItsDataValue() {
        onLegacyServer();
        assertEquals(Material.LEGACY_WOOL, EranoMaterial.RED_WOOL.parseMaterial());
        assertEquals(14, EranoMaterial.RED_WOOL.data());
        assertEquals(Material.LEGACY_STONE, EranoMaterial.GRANITE.parseMaterial());
        assertEquals(1, EranoMaterial.GRANITE.data());
        assertEquals(Material.LEGACY_WOOD_SWORD, EranoMaterial.WOODEN_SWORD.parseMaterial());
        assertEquals(Material.LEGACY_MONSTER_EGG, EranoMaterial.PIG_SPAWN_EGG.parseMaterial());
        assertEquals(90, EranoMaterial.PIG_SPAWN_EGG.data());
        // Newer than 1.12.
        assertNull(EranoMaterial.NETHERITE_INGOT.parseMaterial());
        assertFalse(EranoMaterial.NETHERITE_INGOT.isSupported());
        // The first alternative the server has.
        assertEquals(Optional.of(EranoMaterial.LIME_WOOL), EranoMaterial.match("NETHERITE_BLOCK|LIME_WOOL"));
        assertEquals(Optional.of(EranoMaterial.NETHERITE_BLOCK), EranoMaterial.match("NETHERITE_BLOCK|NOPE"));
    }

    @Test
    void legacyItemsAndBlocksByTheirDataValue() {
        assertEquals(EranoMaterial.RED_WOOL, table.legacyItem("WOOL", 14));
        // Damage isn't a kind.
        assertEquals(EranoMaterial.IRON_SWORD, table.legacyItem("IRON_SWORD", 37));
        // A log's axis (data 4 - 15) isn't a kind either.
        assertEquals(EranoMaterial.BIRCH_LOG, table.legacyBlock("LOG", 2));
        assertEquals(EranoMaterial.BIRCH_LOG, table.legacyBlock("LOG", 6));
        assertEquals(EranoMaterial.OAK_WALL_SIGN, table.legacyBlock("WALL_SIGN", 2));
        assertEquals(EranoMaterial.RED_BED, table.legacyItem("BED", 14));
    }
}
