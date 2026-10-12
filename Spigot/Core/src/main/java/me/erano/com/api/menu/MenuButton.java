package me.erano.com.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

//usage:
//new MenuButton()
//    .creator(player -> new ItemStack(material))   // görünüm
//    .consumer(event -> player.playSound(...))      // davranış
//
//new MenuButton()
//    .creator(player -> new ItemStack(material))
//    .action(player -> ...)                         // envanter event'i olmadan: Bedrock formunda da çalışır

//InventoryButton
// Lightweight version of Gof Builder
// Butonun ne göstereceği ile ne yapacağı birbirinden tamamen ayrıdır ve ikisi de dışarıdan enjekte edilir.
public class MenuButton {

    private Function<Player, ItemStack> iconCreator;
    private Consumer<InventoryClickEvent> eventConsumer;
    private Consumer<Player> action;

    public MenuButton creator(Function<Player, ItemStack> iconCreator) {
        this.iconCreator = iconCreator;
        return this;
    }

    public MenuButton consumer(Consumer<InventoryClickEvent> eventConsumer) {
        this.eventConsumer = eventConsumer;
        return this;
    }

    /**
     * What a click does, given only who clicked: runs from the chest GUI (inside the click event, when there's no
     * {@link #consumer}) and from the Bedrock form of an {@link InventoryMenu#setBedrockForm(boolean) InventoryMenu}.
     */
    public MenuButton action(Consumer<Player> action) {
        this.action = action;
        return this;
    }

    public Consumer<Player> getAction() {
        return this.action;
    }

    public Consumer<InventoryClickEvent> getEventConsumer() {
        return this.eventConsumer;
    }

    public Function<Player, ItemStack> getIconCreator() {
        return this.iconCreator;
    }
}
