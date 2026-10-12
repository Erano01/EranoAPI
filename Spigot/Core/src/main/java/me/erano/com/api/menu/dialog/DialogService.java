package me.erano.com.api.menu.dialog;

import org.bukkit.entity.Player;

/**
 * Shows {@link DialogMenu}s. Get it from {@link me.erano.com.api.EranoServices#dialogs()}, or open menus through
 * {@link me.erano.com.api.menu.MenuDispatcher#openMenu}.
 */
public interface DialogService {

    /** Builds {@code menu} for {@code player} ({@link DialogMenu#prepare(Player)}) and shows it. */
    void show(Player player, DialogMenu menu);

    void close(Player player);

    /** False before 1.21.6, where {@link #show} throws {@link UnsupportedOperationException}. */
    default boolean isSupported() {
        return true;
    }
}
