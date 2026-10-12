package me.erano.com.common.menu;

/**
 * A screen shown to a player of type {@code P} (Bukkit's {@code Player}, Nukkit's {@code Player} ...).
 *
 * <p>Each platform opens the menu types it supports through its own dispatcher: Spigot / Paper open
 * {@code InventoryMenu}s (chest GUI) and {@link me.erano.com.common.menu.dialog.DialogMenu}s (1.21.6+),
 * Bedrock shows dialog menus as forms.
 *
 * @param <P> the platform's player type
 */
public interface Menu<P> {
}
