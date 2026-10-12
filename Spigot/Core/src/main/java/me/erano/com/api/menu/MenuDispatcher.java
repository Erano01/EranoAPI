package me.erano.com.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import me.erano.com.api.EranoServices;
import me.erano.com.common.menu.Menu;
import me.erano.com.common.menu.dialog.DialogMenu;

import java.util.HashMap;
import java.util.Map;

// MenuListener'imiz Bukkit Event'larını (subject'lerini) MenuListener'a abone ederken,
// MenuDispatcher'a passlıyor hangi subject'in ne yapması gerektiğini.
public class MenuDispatcher {

    // Registry - Her bir inventory için InventoryHandler(menudeki davranışları temsil eden arayüz) mapleniyor.
    // Oyuncular tarafından açılan tüm inventory'leri cache'ler bu registry.
    private final Map<Inventory, InventoryHandler> activeInventories = new HashMap<>();

    // Trigger method - Herhangi bir yerden kullanılabilir menü sistemi bu sayede:
    // menuDispatcher.openMenu(new PianoMenu(), player);   -> chest GUI
    // menuDispatcher.openMenu(new SettingsDialog(), player); -> dialog (1.21.6+)
    // Menu Common'da platformdan bağımsız; Bukkit'in açabildiği tipler InventoryMenu ve DialogMenu.
    public void openMenu(Menu<Player> menu, Player player) {
        if (menu instanceof InventoryMenu) {
            this.openInventory((InventoryMenu) menu, player);
        } else if (menu instanceof DialogMenu) {
            this.openDialog((DialogMenu<Player>) menu, player);
        } else {
            throw new IllegalArgumentException("Bukkit can't open " + menu.getClass().getName());
        }
    }

    // EranoAPI 1.0.0-alpha.3 ve öncesine karşı derlenmiş plugin'ler bu imzayı çağırır.
    /** @deprecated use {@link #openMenu(Menu, Player)} with an {@link InventoryMenu} */
    @Deprecated
    public void openMenu(me.erano.com.api.menu.Menu menu, Player player) {
        this.openInventory(menu, player);
    }

    // Registry'e entry olarak eklenir ve oyuncunun ekranına Inventory açar.
    // Bedrock oyuncusuna setBedrockForm(true) menüler form olarak gösterilir.
    public void openInventory(InventoryMenu menu, Player player) {
        if (menu.isBedrockForm() && EranoServices.bedrock().isBedrock(player)) {
            EranoServices.bedrock().forms().show(player, new InventoryFormMenu(menu));
            return;
        }
        this.registerHandledInventory(menu.getInventory(), menu);
        player.openInventory(menu.getInventory());
    }

    // Dialog'un buton callback'lerini dialog servisi tutar (EranoServices.dialogs()), registry'e gerek yok.
    // Dialog desteklenmiyorsa (1.21.6 öncesi) UnsupportedOperationException fırlatır; önce
    // EranoServices.dialogs().isSupported() ile kontrol edip InventoryMenu'ye düşebilirsin.
    public void openDialog(DialogMenu<Player> menu, Player player) {
        EranoServices.dialogs().show(player, menu);
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

    public void handleDrag(InventoryDragEvent event) {
        InventoryHandler handler = this.activeInventories.get(event.getInventory());
        if (handler != null) {
            handler.onDrag(event);
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
