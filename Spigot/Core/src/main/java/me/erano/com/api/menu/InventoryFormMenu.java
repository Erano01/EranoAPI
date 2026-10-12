package me.erano.com.api.menu;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.erano.com.common.menu.dialog.DialogButton;
import me.erano.com.common.menu.dialog.DialogMenu;

/** An {@link InventoryMenu} as a Bedrock form, see {@link InventoryMenu#setBedrockForm(boolean)}. */
final class InventoryFormMenu extends DialogMenu<Player> {

    private final InventoryMenu menu;

    InventoryFormMenu(InventoryMenu menu) {
        super(menu.getTitle());
        this.menu = menu;
    }

    @Override
    protected void decorate(Player player) {
        menu.decorate(player);
        for (Map.Entry<Integer, MenuButton> entry : menu.getButtons().entrySet()) {
            MenuButton button = entry.getValue();
            if (button.getAction() == null) {
                continue;
            }
            addButton(new DialogButton<Player>()
                    .label(label(menu.getInventory().getItem(entry.getKey())))
                    .consumer(click -> button.getAction().accept(click.getPlayer())));
        }
    }

    // The icon's name and the last line of its lore (what clicking does), like the chest GUI shows them.
    private static String label(ItemStack icon) {
        if (icon == null || !icon.hasItemMeta()) {
            return icon == null ? "" : icon.getType().name();
        }
        ItemMeta meta = icon.getItemMeta();
        String name = meta.hasDisplayName() ? meta.getDisplayName() : icon.getType().name();
        List<String> lore = meta.getLore();
        if (lore == null || lore.isEmpty() || lore.get(lore.size() - 1).isEmpty()) {
            return name;
        }
        return name + "\n§r" + lore.get(lore.size() - 1);
    }
}
