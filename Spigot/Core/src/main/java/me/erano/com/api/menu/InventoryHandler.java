package me.erano.com.api.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

//This interface provides the 3 behaviors required for the Menu via Inventory event parameters.
public interface InventoryHandler {

    void onClick(InventoryClickEvent event);

    void onOpen(InventoryOpenEvent event);

    void onClose(InventoryCloseEvent event);

}
