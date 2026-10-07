package me.erano.com.api.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;


//GUIListener
// observer
public class MenuListener implements Listener {

    private final MenuDispatcher menuDispatcher;

    public MenuListener(MenuDispatcher menuDispatcher) {
        this.menuDispatcher = menuDispatcher;
    }

    // Bukkit event sisteminin subjectlerini kendimize abone ediyoruz aşağıda ki @EventHandler'lar ile.
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        this.menuDispatcher.handleClick(event);
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        this.menuDispatcher.handleOpen(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        this.menuDispatcher.handleClose(event);
    }

}
