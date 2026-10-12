package me.erano.com.api.menu;

/**
 * The inventory menu of EranoAPI 1.0.0-alpha.3 and before, kept so plugins built against it still run.
 *
 * @deprecated use {@link InventoryMenu}; {@code Menu} is now {@link me.erano.com.common.menu.Menu}, the common
 *             interface of inventory and dialog menus.
 */
@Deprecated
public abstract class Menu extends InventoryMenu {

    public Menu() {
        super();
    }

    public Menu(int size, String title) {
        super(size, title);
    }
}
