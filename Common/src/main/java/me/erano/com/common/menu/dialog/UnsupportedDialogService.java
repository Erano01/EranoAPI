package me.erano.com.common.menu.dialog;

/** Used where dialogs can't be shown (Java Edition before 1.21.6): use an inventory menu there. */
public final class UnsupportedDialogService<P> implements DialogService<P> {

    @Override
    public void show(P player, DialogMenu<P> menu) {
        throw new UnsupportedOperationException("Dialogs need Minecraft 1.21.6+, use an InventoryMenu");
    }

    @Override
    public void close(P player) {
    }

    @Override
    public boolean isSupported() {
        return false;
    }
}
