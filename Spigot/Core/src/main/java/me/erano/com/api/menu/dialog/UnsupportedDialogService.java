package me.erano.com.api.menu.dialog;

import org.bukkit.entity.Player;

/** Used before 1.21.6: Minecraft has no dialogs there, use an {@link me.erano.com.api.menu.InventoryMenu}. */
public final class UnsupportedDialogService implements DialogService {

    @Override
    public void show(Player player, DialogMenu menu) {
        throw new UnsupportedOperationException("Dialogs need Minecraft 1.21.6+, use an InventoryMenu");
    }

    @Override
    public void close(Player player) {
    }

    @Override
    public boolean isSupported() {
        return false;
    }
}
