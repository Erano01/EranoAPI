package me.erano.com.api.display;

import org.bukkit.entity.Player;

/**
 * Action bar and title messages on every version: Bukkit API where it exists (action bar 1.9+, titles with
 * timings 1.11+), packets before that. Get it from {@link me.erano.com.api.EranoServices#messages()}.
 *
 * <p>Text uses legacy color codes ({@code §a}); {@code null} counts as empty. Times are in ticks.
 */
public interface MessageService {

    void sendActionBar(Player player, String message);

    /** An empty {@code title} with a non-empty {@code subtitle} shows only the subtitle. */
    void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);

    default void clearTitle(Player player) {
        player.resetTitle();
    }

    /** Always true; kept for symmetry with the services that can be missing on some versions. */
    default boolean isSupported() {
        return true;
    }
}
