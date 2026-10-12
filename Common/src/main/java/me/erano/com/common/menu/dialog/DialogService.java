package me.erano.com.common.menu.dialog;

/**
 * Shows {@link DialogMenu}s on one platform. On Spigot / Paper get it from {@code EranoServices#dialogs()},
 * or open menus through {@code MenuDispatcher#openMenu}.
 *
 * @param <P> the platform's player type
 */
public interface DialogService<P> {

    /** Builds {@code menu} for {@code player} ({@link DialogMenu#prepare(Object)}) and shows it. */
    void show(P player, DialogMenu<P> menu);

    void close(P player);

    /** False where the platform can't show dialogs (Java Edition before 1.21.6); {@link #show} then throws {@link UnsupportedOperationException}. */
    default boolean isSupported() {
        return true;
    }

    /** Whether {@code player} can be shown dialogs; a Bedrock player can, as forms, on every version. */
    default boolean isSupported(P player) {
        return isSupported();
    }
}
