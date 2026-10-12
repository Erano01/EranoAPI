package me.erano.com.api.menu;

import org.bukkit.entity.Player;

/**
 * A screen a player can be shown through {@link MenuDispatcher#openMenu(Menu, Player)}:
 * an {@link InventoryMenu} (chest GUI, every version) or a {@link me.erano.com.api.menu.dialog.DialogMenu}
 * (Minecraft's dialog screen, 1.21.6+).
 */
public interface Menu {

    // Double dispatch: her menü tipi dispatcher'ın kendine ait open metodunu çağırır.
    void open(Player player, MenuDispatcher dispatcher);
}
