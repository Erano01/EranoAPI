package me.erano.com.api;

import java.util.function.Function;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import me.erano.com.api.bossbar.BossBarService;
import me.erano.com.api.bossbar.BossBarServiceProvider;
import me.erano.com.api.bossbar.PacketBossBarService;
import me.erano.com.api.bossbar.UnsupportedBossBarService;
import me.erano.com.api.display.BukkitMessageService;
import me.erano.com.api.display.MessageService;
import me.erano.com.api.display.MessageServiceProvider;
import me.erano.com.api.hologram.AbstractHologramService;
import me.erano.com.api.hologram.HologramService;
import me.erano.com.api.hologram.HologramServiceProvider;
import me.erano.com.api.hologram.UnsupportedHologramService;
import me.erano.com.common.VersionedService;
import me.erano.com.common.VersionedServices;

/**
 * Entry point to the version independent services. Each one is picked for the running server when EranoAPI
 * is enabled; a service without a provider for this version reports {@code isSupported() == false} instead
 * of failing.
 */
public final class EranoServices {

    private static MessageService messages;
    private static HologramService holograms;
    private static BossBarService bossBars;

    private EranoServices() {
    }

    /** Action bar and titles. */
    public static MessageService messages() {
        return require(messages);
    }

    /** Bars at the top of the screen, 1.8 included. */
    public static BossBarService bossBars() {
        return require(bossBars);
    }

    /** Client side text markers. */
    public static HologramService holograms() {
        return require(holograms);
    }

    static void enable(Plugin plugin) {
        messages = select(plugin, MessageServiceProvider.class, MessageServiceProvider::create, new BukkitMessageService());
        holograms = select(plugin, HologramServiceProvider.class, provider -> provider.create(plugin), new UnsupportedHologramService());
        bossBars = select(plugin, BossBarServiceProvider.class, provider -> provider.create(plugin), new UnsupportedBossBarService());
    }

    static void disable() {
        if (holograms instanceof AbstractHologramService) {
            ((AbstractHologramService) holograms).shutdown();
        }
        if (bossBars instanceof PacketBossBarService) {
            ((PacketBossBarService) bossBars).shutdown();
        }
        messages = null;
        holograms = null;
        bossBars = null;
    }

    private static <P extends VersionedService, S> S select(Plugin plugin, Class<P> type, Function<P, S> create, S fallback) {
        try {
            P provider = VersionedServices.select(type, EranoServices.class.getClassLoader(), ServerVersion.current());
            plugin.getLogger().fine(type.getSimpleName() + ": " + provider.getClass().getName());
            return create.apply(provider);
        } catch (RuntimeException | LinkageError e) {
            plugin.getLogger().log(Level.WARNING, type.getSimpleName() + " is not supported on Minecraft " + ServerVersion.current(), e);
            return fallback;
        }
    }

    private static <S> S require(S service) {
        if (service == null) {
            throw new IllegalStateException("EranoAPI is not enabled; add depend: [EranoAPI] to your plugin.yml");
        }
        return service;
    }
}
