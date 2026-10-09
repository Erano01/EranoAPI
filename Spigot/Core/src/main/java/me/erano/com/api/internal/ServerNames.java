package me.erano.com.api.internal;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * What each constant of a {@link NameTable} is on this server: the first of its names the server has. Looked up once
 * per constant. Internal to EranoAPI.
 *
 * @param <T> the server's type ({@code Sound}, {@code Enchantment} ...)
 */
public final class ServerNames<E extends Enum<E>, T> {

    private static final Object MISSING = new Object();

    private final Class<E> type;
    private final NameTable<E> table;
    private final Function<String, T> lookup;
    private final Map<E, Object> resolved;
    /** The name the server knew it by. */
    private final Map<E, String> matchedNames;
    private Map<T, E> reverse;

    /** @param lookup the server's value of a name, {@code null} if it has none */
    public ServerNames(Class<E> type, NameTable<E> table, Function<String, T> lookup) {
        this.type = type;
        this.table = table;
        this.lookup = lookup;
        this.resolved = new EnumMap<>(type);
        this.matchedNames = new EnumMap<>(type);
    }

    /** @return {@code null} if the server has none of its names */
    @SuppressWarnings("unchecked")
    public synchronized T get(E constant) {
        Object value = resolved.get(constant);
        if (value == null) {
            value = MISSING;
            for (String name : table.names(constant)) {
                T found = lookup.apply(name);
                if (found != null) {
                    value = found;
                    matchedNames.put(constant, name);
                    break;
                }
            }
            resolved.put(constant, value);
        }
        return value == MISSING ? null : (T) value;
    }

    /**
     * Which constant the server's value is. Several constants can share one value on an older server (on 1.8
     * {@code ENTITY_ENDER_DRAGON_AMBIENT} and {@code ENTITY_ENDER_DRAGON_GROWL} are both {@code ENDERDRAGON_GROWL}):
     * the one {@link NameTable#match} gives for that name, else the first.
     */
    public synchronized E of(T value) {
        if (value == null) {
            return null;
        }
        if (reverse == null) {
            reverse = new HashMap<>();
            for (E constant : type.getEnumConstants()) {
                T server = get(constant);
                if (server != null && (!reverse.containsKey(server)
                        || table.match(matchedNames.get(constant)).orElse(null) == constant)) {
                    reverse.put(server, constant);
                }
            }
        }
        return reverse.get(value);
    }

    /** The server's constant named so in {@code owner}: a public static field, whether {@code owner} is an enum or not. */
    public static <T> Function<String, T> staticFields(Class<?> owner, Class<T> type) {
        return name -> {
            try {
                Object value = owner.getField(name).get(null);
                return type.isInstance(value) ? type.cast(value) : null;
            } catch (ReflectiveOperationException | RuntimeException | LinkageError e) {
                return null; // not on this server (or no server: registry-backed types can't initialize)
            }
        };
    }
}
