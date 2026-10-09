package me.erano.com.api.bossbar;

/**
 * Bars at the top of the screen, on every version: Bukkit's boss bars on 1.9+, and on 1.8 (which has no boss bar API)
 * the health bar of a wither that exists only on the viewer's client. Get it from
 * {@link me.erano.com.api.EranoServices#bossBars()}.
 * <p>
 * On 1.8 the client shows one boss bar at a time (a player in two bars sees the last one they were added to), the bar
 * is always the wither's purple, and the screen's fog is a little darker while it shows.
 */
public interface BossBarService {

    /**
     * A new bar, shown to nobody yet.
     *
     * @param color a Bukkit {@code BarColor} name (YELLOW, RED ...); ignored on 1.8. Unknown names are YELLOW.
     */
    GameBar create(String title, String color);

    /** {@code false} when no implementation supports the server: bars then keep their state but show nothing. */
    boolean isSupported();

    /** The bar of the 1.8 client: one at a time, no colors. */
    boolean isLegacy();
}
