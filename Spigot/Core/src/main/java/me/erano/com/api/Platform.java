package me.erano.com.api;

/**
 * The server software EranoAPI runs on, told apart by classes only that software has.
 *
 * <p>Paper's NMS providers ({@code Paper/NMS}) are only available where {@link #isPaper()} is true. From
 * 1.20.5 to 1.21.11 Paper runs on Mojang's names, so the Spigot NMS providers of those revisions are only
 * available where it's false.
 */
public final class Platform {

    private static final boolean PAPER = anyClassExists(
            "io.papermc.paper.configuration.GlobalConfiguration", // 1.19+
            "com.destroystokyo.paper.PaperConfig",                // 1.9 - 1.18
            "org.github.paperspigot.PaperSpigotConfig");          // 1.8.8 (PaperSpigot)

    private static final boolean PURPUR = anyClassExists(
            "org.purpurmc.purpur.PurpurConfig",
            "net.pl3x.purpur.PurpurConfig");

    private Platform() {
    }

    /** Paper or a fork of it (Purpur ...). */
    public static boolean isPaper() {
        return PAPER;
    }

    /** Purpur, a fork of Paper: {@link #isPaper()} is true there too. */
    public static boolean isPurpur() {
        return PURPUR;
    }

    private static boolean anyClassExists(String... names) {
        for (String name : names) {
            try {
                Class.forName(name, false, Platform.class.getClassLoader());
                return true;
            } catch (ClassNotFoundException ignored) {
            }
        }
        return false;
    }
}
