package me.erano.com.api.internal;

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

/**
 * A generated table of a versioned API ({@code EranoSound}, {@code EranoEnchantment} ...), read from the jar once;
 * nothing here asks the server. One row per constant: its name, the names it had on older servers (newest first,
 * tried in this order on a server), the older names that {@link #match} reads as this constant, its Minecraft key,
 * the first NMS revision that has it and one free column. Internal to EranoAPI.
 */
public final class NameTable<E extends Enum<E>> {

    private final Map<E, List<String>> names;
    private final Map<E, String> keys;
    private final Map<E, String> firsts;
    private final Map<E, String> extras;
    /** Normalized name, older name or key -> constant. */
    private final Map<String, E> byText = new HashMap<>();

    private NameTable(Class<E> type) {
        names = new EnumMap<>(type);
        keys = new EnumMap<>(type);
        firsts = new EnumMap<>(type);
        extras = new EnumMap<>(type);
    }

    /** Columns: name, older names, matched older names, key, first revision, extra; lists comma separated. */
    public static <E extends Enum<E>> NameTable<E> load(Class<E> type, String resource) {
        NameTable<E> table = new NameTable<>(type);
        List<String[]> rows = read(type, resource);
        for (String[] row : rows) {
            E constant = constant(type, row[0]);
            if (constant == null) {
                continue;
            }
            List<String> all = new ArrayList<>();
            all.add(row[0]);
            all.addAll(split(row[1]));
            table.names.put(constant, Collections.unmodifiableList(all));
            table.keys.put(constant, row[3]);
            table.firsts.put(constant, row[4]);
            table.extras.put(constant, row.length > 5 ? row[5] : "");
            table.byText.put(normalize(row[0]), constant);
        }
        // Older names and keys only where no constant has that name today
        for (String[] row : rows) {
            E constant = constant(type, row[0]);
            if (constant == null) {
                continue;
            }
            for (String old : split(row[2])) {
                table.byText.putIfAbsent(normalize(old), constant);
            }
            if (!row[3].isEmpty()) {
                table.byText.putIfAbsent(normalize(row[3]), constant);
            }
        }
        return table;
    }

    /** The constant's name, then its older names, newest first. */
    public List<String> names(E constant) {
        List<String> list = names.get(constant);
        return list == null ? Collections.singletonList(constant.name()) : list;
    }

    public List<String> oldNames(E constant) {
        List<String> list = names(constant);
        return list.subList(1, list.size());
    }

    /** The Minecraft key without namespace, {@code ""} if none. */
    public String key(E constant) {
        String key = keys.get(constant);
        return key == null ? "" : key;
    }

    /** The first NMS revision that has it, e.g. {@code V1_9_R1}. */
    public String first(E constant) {
        String first = firsts.get(constant);
        return first == null ? "" : first;
    }

    public String extra(E constant) {
        String extra = extras.get(constant);
        return extra == null ? "" : extra;
    }

    /**
     * Today's name, an older name, the Minecraft key ({@code minecraft:} optional); case, spaces, dashes and dots
     * don't matter.
     */
    public Optional<E> match(String text) {
        if (text == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(byText.get(normalize(text)));
    }

    static String normalize(String text) {
        String name = text.trim();
        if (name.regionMatches(true, 0, "minecraft:", 0, 10)) {
            name = name.substring(10);
        }
        StringBuilder out = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            out.append(c == ' ' || c == '-' || c == '.' ? '_' : c);
        }
        return out.toString().toUpperCase(Locale.ROOT);
    }

    private static List<String> split(String column) {
        if (column.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> list = new ArrayList<>();
        for (String part : column.split(",")) {
            list.add(part);
        }
        return list;
    }

    private static <E extends Enum<E>> E constant(Class<E> type, String name) {
        try {
            return Enum.valueOf(type, name);
        } catch (IllegalArgumentException e) {
            return null; // a table newer than the enum
        }
    }

    private static List<String[]> read(Class<?> owner, String resource) {
        InputStream in = owner.getResourceAsStream(resource);
        if (in == null) {
            throw new IllegalStateException("EranoAPI's table " + resource + " is missing from the jar");
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
            throw new IllegalStateException("can't read EranoAPI's table " + resource, e);
        }
        return rows;
    }
}
