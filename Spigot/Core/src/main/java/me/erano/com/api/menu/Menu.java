package me.erano.com.api.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Menu implements InventoryHandler {

    private final int size;
    private final String title;
    private Inventory inventory;
    private final Map<Integer, MenuButton> buttonMap = new HashMap<>();

    // Alt sınıf createInventory()'yi override etmek zorunda.
    public Menu() {
        this(-1, null);
    }

    // createInventory() override edilmeden Bukkit.createInventory(null, size, title) kullanılır.
    public Menu(int size, String title) {
        this.size = size;
        this.title = title;
    }

    // Bukkit Inventory oluşturma. Lazy çağrılır (ilk getInventory()'de), böylece alt sınıf alanları atanmış olur.
    protected Inventory createInventory() {
        if (this.size < 0) {
            throw new IllegalStateException(getClass().getName() + " must use Menu(int, String) or override createInventory()");
        }
        return Bukkit.createInventory(null, this.size, this.title);
    }

    public Inventory getInventory() {
        if (this.inventory == null) {
            this.inventory = this.createInventory();
        }
        return this.inventory;
    }

    // buttonMap'e yeni bir entry ekler.
    public void addButton(int slot, MenuButton button) {
        this.buttonMap.put(slot, button);
    }

    // buttonMap içerisindeki düzeni Inventory'a aktarıyor.
    public void decorate(Player player) {
        this.buttonMap.forEach((slot, button) -> {
            Function<Player, ItemStack> iconCreator = button.getIconCreator();
            this.getInventory().setItem(slot, iconCreator == null ? null : iconCreator.apply(player));
        });
    }

    // InventoryHandler'in davranışları burada implement ediliyor override edilerek.
    // Alt envanter (oyuncunun kendi envanteri) tıklamaları butonları tetiklemez; sadece menüye eşya
    // taşıyabilecek olanlar (shift-click, double-click toplama) iptal edilir.
    @Override
    public void onClick(InventoryClickEvent event) {
        int rawSlot = event.getRawSlot();
        if (rawSlot < 0 || rawSlot >= this.getInventory().getSize()) {
            switch (event.getAction()) {
                case MOVE_TO_OTHER_INVENTORY:
                case COLLECT_TO_CURSOR:
                    event.setCancelled(true);
                    break;
                default:
                    break;
            }
            return;
        }
        event.setCancelled(true);
        MenuButton button = this.buttonMap.get(rawSlot);
        if (button != null) {
            Consumer<InventoryClickEvent> consumer = button.getEventConsumer();
            if (consumer != null) {
                consumer.accept(event);
            }
        }
    }

    // Menüye eşya sürüklenmesin.
    @Override
    public void onDrag(InventoryDragEvent event) {
        int size = this.getInventory().getSize();
        for (int rawSlot : event.getRawSlots()) {
            if (rawSlot < size) {
                event.setCancelled(true);
                return;
            }
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
