package me.erano.com.api;

import org.bukkit.Bukkit;

import me.erano.com.common.MinecraftVersion;

/** The Minecraft version of the running server. */
public final class ServerVersion {

    private static MinecraftVersion current;

    private ServerVersion() {
    }

    /** Parsed from {@link Bukkit#getBukkitVersion()}, e.g. {@code 1.21.11-R0.2-SNAPSHOT} or {@code 26.1.2-R0.1-SNAPSHOT}. */
    public static synchronized MinecraftVersion current() {
        if (current == null) {
            current = MinecraftVersion.parse(Bukkit.getBukkitVersion());
        }
        return current;
    }
}
