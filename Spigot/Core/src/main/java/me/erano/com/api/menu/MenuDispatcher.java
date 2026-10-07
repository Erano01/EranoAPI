package me.erano.com.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

// MenuListener'imiz Bukkit Event'larını (subject'lerini) MenuListener'a abone ederken,
// MenuDispatcher'a passlıyor hangi subject'in ne yapması gerektiğini.
public class MenuDispatcher {

    // Registry - Her bir inventory için InventoryHandler(menudeki davranışları temsil eden arayüz) mapleniyor.
    // Oyuncular tarafından açılan tüm inventory'leri cache'ler bu registry.
    private final Map<Inventory, InventoryHandler> activeInventories = new HashMap<>();

    // Trigger method - Registry'e entry olarak eklenir ve oyuncunun ekranına Inventory açar.
    // Herhangi bir yerden kullanılabilir menü sistemi bu sayede -> menuDispatcher.openMenu(new PianoMenu(), player);
    public void openMenu(Menu menu, Player player) {
        this.registerHandledInventory(menu.getInventory(), menu);
        player.openInventory(menu.getInventory());
    }

    // Yeni bir registry entry'si eklemek için kullanılan method.
    public void registerHandledInventory(Inventory inventory, InventoryHandler handler) {
        this.activeInventories.put(inventory, handler);
    }

    // Registry'den bir entry silmek için kullanılan method.
    public void unregisterInventory(Inventory inventory) {
        this.activeInventories.remove(inventory);
    }

    // Aşağıda ki 3 method için:
    // Subject'in kullandığı Inventory'nin kime ait olduğunu registry'e soruyor, eğer bu inventory registry'de kayıtlı ise
    // Handler'in override edilecek olan davranışını execute ediyor.
    // Menu(abstract class) extends InventoryHandler.
    public void handleClick(InventoryClickEvent event) {
        InventoryHandler handler = this.activeInventories.get(event.getInventory());
        if (handler != null) {
            handler.onClick(event);
        }
    }

    public void handleOpen(InventoryOpenEvent event) {
        InventoryHandler handler = this.activeInventories.get(event.getInventory());
        if (handler != null) {
            handler.onOpen(event);
        }
    }
    //Menu kapatıldığında Inventory ve InventoryHandler key, value mapi cachedan çıkartılıyor.
    public void handleClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHandler handler = this.activeInventories.get(inventory);
        if (handler != null) {
            handler.onClose(event);
            this.unregisterInventory(inventory);
        }
    }

}
