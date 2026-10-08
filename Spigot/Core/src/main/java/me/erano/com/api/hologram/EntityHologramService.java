package me.erano.com.api.hologram;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * 1.13+: real entities with {@code setPersistent(false)}, so they're never saved with the chunk. One
 * {@code TextDisplay} on 1.19.4+, one marker armor stand per line before that.
 */
public class EntityHologramService extends AbstractHologramService {

    /** How non-viewers are kept from seeing the entities. */
    enum Visibility {
        /** {@code Entity#setVisibleByDefault(false)} + {@code Player#showEntity}, 1.19.3+: no packet to non-viewers. */
        HIDDEN_BY_DEFAULT,
        /** {@code Player#hideEntity} after spawning, 1.18 - 1.19.2. */
        HIDE_FROM_OTHERS,
        /** 1.13 - 1.17: everyone sees it. */
        EVERYONE
    }

    private final Visibility visibility;
    private final boolean textDisplay;

    public EntityHologramService(Plugin plugin) {
        super(plugin);
        this.visibility = hasMethod(Entity.class, "setVisibleByDefault", boolean.class) ? Visibility.HIDDEN_BY_DEFAULT
                : hasMethod(Player.class, "hideEntity", Plugin.class, Entity.class) ? Visibility.HIDE_FROM_OTHERS
                : Visibility.EVERYONE;
        this.textDisplay = classExists("org.bukkit.entity.TextDisplay");
    }

    @Override
    protected AbstractHologram create(Location at, List<String> lines) {
        // TextDisplayHologram is only loaded when the class exists.
        return textDisplay ? new TextDisplayHologram(this, at, lines) : new ArmorStandHologram(this, at, lines);
    }

    @Override
    public boolean isPerViewer() {
        return visibility != Visibility.EVERYONE;
    }

    Visibility visibility() {
        return visibility;
    }

    Plugin plugin() {
        return plugin;
    }

    private static boolean hasMethod(Class<?> type, String name, Class<?>... parameters) {
        try {
            type.getMethod(name, parameters);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static boolean classExists(String name) {
        try {
            Class.forName(name, false, EntityHologramService.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
