package me.erano.com.api.material;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * The generated tables, read from the jar once; nothing here asks the server. {@code materials.tsv}: each material,
 * its 1.8 - 1.12 name and data value, its older names. {@code legacy.tsv}: each 1.8 - 1.12 name and data value, as a
 * block and as an item, by today's name.
 */
final class MaterialTable {

    private static final String FOLDER = "/eranoapi/material/";

    private final Map<EranoMaterial, String> legacyNames = new EnumMap<>(EranoMaterial.class);
    private final Map<EranoMaterial, Short> legacyData = new EnumMap<>(EranoMaterial.class);
    private final Map<EranoMaterial, List<String>> oldNames = new EnumMap<>(EranoMaterial.class);
    /** Older name -> material. */
    private final Map<String, EranoMaterial> byOldName = new HashMap<>();
    /** "NAME:data" of 1.8 - 1.12 -> material, as a block / as an item. */
    private final Map<String, EranoMaterial> legacyBlocks = new HashMap<>();
    private final Map<String, EranoMaterial> legacyItems = new HashMap<>();

    MaterialTable() {
        for (String[] row : read("materials.tsv")) {
            EranoMaterial material = constant(row[0]);
            if (material == null) {
                continue;
            }
            if (!row[1].isEmpty()) {
                legacyNames.put(material, row[1]);
                legacyData.put(material, Short.parseShort(row[2]));
            }
            List<String> old = new ArrayList<>();
            if (row.length > 3 && !row[3].isEmpty()) {
                for (String name : row[3].split(",")) {
                    old.add(name);
                    byOldName.put(name, material);
                }
            }
            oldNames.put(material, old);
        }
        for (String[] row : read("legacy.tsv")) {
            String key = row[0] + ':' + row[1];
            EranoMaterial block = constant(row[2]);
            EranoMaterial item = row.length > 3 ? constant(row[3]) : null;
            if (block != null) {
                legacyBlocks.put(key, block);
            }
            if (item != null) {
                legacyItems.put(key, item);
            }
        }
    }

    private static List<String[]> read(String file) {
        InputStream in = MaterialTable.class.getResourceAsStream(FOLDER + file);
        if (in == null) {
            throw new IllegalStateException("EranoAPI's material table " + FOLDER + file + " is missing from the jar");
        }
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty() && line.charAt(0) != '#') {
                    rows.add(line.split("\t", -1));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't read EranoAPI's material table " + file, e);
        }
        return rows;
    }

    static EranoMaterial constant(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        try {
            return EranoMaterial.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    String legacyName(EranoMaterial material) {
        String name = legacyNames.get(material);
        return name == null ? "" : name;
    }

    short legacyData(EranoMaterial material) {
        Short data = legacyData.get(material);
        return data == null ? 0 : data;
    }

    List<String> oldNames(EranoMaterial material) {
        List<String> names = oldNames.get(material);
        return names == null ? Collections.<String>emptyList() : names;
    }

    /** A 1.8 - 1.12 item by name and data value; data values that aren't kinds (damage) fall back to 0. */
    EranoMaterial legacyItem(String name, int data) {
        EranoMaterial material = legacyItems.get(name + ':' + data);
        return material != null ? material : legacyItems.get(name + ":0");
    }

    /** A 1.8 - 1.12 block by name and data value; data values that aren't kinds (facing, axis) fall back to 0. */
    EranoMaterial legacyBlock(String name, int data) {
        EranoMaterial material = legacyBlocks.get(name + ':' + data);
        return material != null ? material : legacyBlocks.get(name + ":0");
    }

    /** See {@link EranoMaterial#match(String)}. */
    Optional<EranoMaterial> match(String name, Predicate<EranoMaterial> supported) {
        if (name == null) {
            return Optional.empty();
        }
        EranoMaterial firstKnown = null;
        for (String alternative : name.split("\\|")) {
            EranoMaterial material = matchOne(alternative);
            if (material == null) {
                continue;
            }
            if (supported.test(material)) {
                return Optional.of(material);
            }
            if (firstKnown == null) {
                firstKnown = material;
            }
        }
        return Optional.ofNullable(firstKnown);
    }

    private EranoMaterial matchOne(String text) {
        String name = text.trim().toUpperCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
        if (name.startsWith("MINECRAFT:")) {
            name = name.substring("MINECRAFT:".length());
        }
        if (name.isEmpty()) {
            return null;
        }
        int colon = name.indexOf(':');
        if (colon > 0) {
            int data;
            try {
                data = Integer.parseInt(name.substring(colon + 1));
            } catch (NumberFormatException e) {
                return null;
            }
            String legacy = name.substring(0, colon);
            EranoMaterial item = legacyItems.get(legacy + ':' + data);
            return item != null ? item : legacyBlocks.get(legacy + ':' + data);
        }
        EranoMaterial material = constant(name);
        if (material != null) {
            return material;
        }
        // An older name since 1.13 before a 1.12 one: GRASS was short grass on 1.13 - 1.20.2 (the 1.12 grass block is
        // GRASS_BLOCK).
        material = byOldName.get(name);
        if (material != null) {
            return material;
        }
        EranoMaterial item = legacyItems.get(name + ":0");
        return item != null ? item : legacyBlocks.get(name + ":0");
    }
}
