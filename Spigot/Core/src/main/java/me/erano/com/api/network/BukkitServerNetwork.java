package me.erano.com.api.network;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;

import me.erano.com.common.network.Channels;
import me.erano.com.common.network.ServerNetwork;

/**
 * {@link ServerNetwork} on the Bukkit plugin messaging API (1.8+, no NMS). Channels are registered
 * for the given plugin, so they show up as that plugin's.
 *
 * <p>Before 1.13 Bukkit limits channel names to 20 characters.
 */
public final class BukkitServerNetwork implements ServerNetwork {

    private final Plugin plugin;
    private final Set<String> outgoing = new HashSet<>();
    private final Set<String> incoming = new HashSet<>();

    private BukkitServerNetwork(Plugin plugin) {
        this.plugin = plugin;
    }

    /** @param plugin the plugin that owns the channels */
    public static ServerNetwork create(Plugin plugin) {
        return new BukkitServerNetwork(plugin);
    }

    @Override
    public void registerOutgoing(String channel) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, Channels.validate(channel));
        outgoing.add(channel);
    }

    @Override
    public void registerIncoming(String channel, BiConsumer<UUID, byte[]> handler) {
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, Channels.validate(channel),
                (ch, player, message) -> handler.accept(player.getUniqueId(), message));
        incoming.add(channel);
    }

    @Override
    public boolean canSend(UUID player, String channel) {
        Player online = Bukkit.getPlayer(player);
        return online != null && online.getListeningPluginChannels().contains(channel);
    }

    @Override
    public void send(UUID player, String channel, byte[] data) {
        if (!outgoing.contains(channel)) {
            throw new IllegalStateException("Channel " + channel + " isn't registered as outgoing");
        }
        Player online = Bukkit.getPlayer(player);
        if (online == null) {
            throw new IllegalStateException("Player " + player + " isn't online");
        }
        online.sendPluginMessage(plugin, channel, data);
    }

    @Override
    public void close() {
        Messenger messenger = Bukkit.getMessenger();
        for (String channel : outgoing) {
            messenger.unregisterOutgoingPluginChannel(plugin, channel);
        }
        for (String channel : incoming) {
            messenger.unregisterIncomingPluginChannel(plugin, channel);
        }
        outgoing.clear();
        incoming.clear();
    }
}
