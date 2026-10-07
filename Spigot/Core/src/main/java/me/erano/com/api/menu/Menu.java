package me.erano.com.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu implements InventoryHandler {

    private final Inventory inventory;
    private final Map<Integer, MenuButton> buttonMap = new HashMap<>();

    public Menu() {
        this.inventory = this.createInventory();
    }

    // Bukkit Inventory oluşturma. Her Menu subclass'ı kendi Inventory'sini oluşturmak zorunda.
    protected abstract Inventory createInventory();
    public Inventory getInventory() {
        return this.inventory;
    }

    // buttonMap'e yeni bir entry ekler.
    public void addButton(int slot, MenuButton button) {
        this.buttonMap.put(slot, button);
    }

    // buttonMap içerisindeki düzeni Inventory'a aktarıyor.
    public void decorate(Player player) {
        this.buttonMap.forEach((slot, button) -> {
            ItemStack icon = button.getIconCreator().apply(player);
            this.inventory.setItem(slot, icon);
        });
    }

    // InventoryHandler'in davranışları burada implement ediliyor override edilerek.
    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getSlot();
        MenuButton button = this.buttonMap.get(slot);
        if (button != null) {
            button.getEventConsumer().accept(event);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        this.decorate((Player) event.getPlayer());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }



}
