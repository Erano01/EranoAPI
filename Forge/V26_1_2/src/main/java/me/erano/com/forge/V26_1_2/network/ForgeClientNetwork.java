package me.erano.com.forge.V26_1_2.network;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.netty.buffer.Unpooled;
import me.erano.com.common.network.Channels;
import me.erano.com.common.network.ClientNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.Identifier;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.network.ChannelRegistrationChangeEvent;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.EventNetworkChannel;

/**
 * {@link ClientNetwork} on raw Forge {@link EventNetworkChannel}s; registered in META-INF/services.
 * SimpleChannel would prefix a discriminator byte that Spigot and Fabric don't expect, an event
 * channel writes the bytes untouched.
 */
public final class ForgeClientNetwork implements ClientNetwork {

    private final Map<String, EventNetworkChannel> channels = new ConcurrentHashMap<>();
    private final Set<String> outgoing = ConcurrentHashMap.newKeySet();
    /** What the server announced; Forge records channels only after ChannelRegistrationChangeEvent. */
    private final Set<String> announced = ConcurrentHashMap.newKeySet();

    private final List<Runnable> joinListeners = new CopyOnWriteArrayList<>();
    private final List<Runnable> disconnectListeners = new CopyOnWriteArrayList<>();
    private final List<Consumer<Set<String>>> channelListeners = new CopyOnWriteArrayList<>();

    public ForgeClientNetwork() {
        ChannelRegistrationChangeEvent.BUS.addListener(this::onChannelRegistrationChange);
        ClientPlayerNetworkEvent.LoggingIn.BUS.addListener(event -> joinListeners.forEach(Runnable::run));
        ClientPlayerNetworkEvent.LoggingOut.BUS.addListener(event -> {
            announced.clear();
            disconnectListeners.forEach(Runnable::run);
        });
    }

    private EventNetworkChannel channel(String channel) {
        // optional(): joining vanilla servers and servers without the channel must keep working.
        return channels.computeIfAbsent(Channels.validate(channel),
                name -> ChannelBuilder.named(Identifier.parse(name)).optional().eventNetworkChannel());
    }

    @Override
    public void registerOutgoing(String channel) {
        channel(channel);
        outgoing.add(channel);
    }

    @Override
    public void registerIncoming(String channel, Consumer<byte[]> handler) {
        channel(channel).addListener((CustomPayloadEvent event) -> {
            FriendlyByteBuf buf = event.getPayload();
            if (buf == null || !event.getSource().isClientSide()) {
                return;
            }
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            event.getSource().setPacketHandled(true);
            // Payload events fire on the network thread.
            event.getSource().enqueueWork(() -> handler.accept(data));
        });
    }

    @Override
    public boolean canSend(String channel) {
        if (announced.contains(channel)) {
            return true;
        }
        EventNetworkChannel registered = channels.get(channel);
        Connection connection = connection();
        return registered != null && connection != null && registered.isRemotePresent(connection);
    }

    @Override
    public void send(String channel, byte[] data) {
        if (!outgoing.contains(channel)) {
            throw new IllegalStateException("Channel " + channel + " isn't registered as outgoing");
        }
        Connection connection = connection();
        if (connection == null) {
            throw new IllegalStateException("Not connected to a server");
        }
        channels.get(channel).send(new FriendlyByteBuf(Unpooled.wrappedBuffer(data)), connection);
    }

    @Override
    public void onJoin(Runnable listener) {
        joinListeners.add(listener);
    }

    @Override
    public void onDisconnect(Runnable listener) {
        disconnectListeners.add(listener);
    }

    @Override
    public void onServerChannelsRegistered(Consumer<Set<String>> listener) {
        channelListeners.add(listener);
    }

    private void onChannelRegistrationChange(ChannelRegistrationChangeEvent event) {
        // Also fires for the integrated server's side of a singleplayer world; only handle ours.
        if (event.getSource().getSending() != PacketFlow.SERVERBOUND) {
            return;
        }
        Set<String> names = event.getChannels().stream().map(Identifier::toString).collect(Collectors.toSet());
        if (event.getType() == ChannelRegistrationChangeEvent.Type.UNREGISTER) {
            announced.removeAll(names);
            return;
        }
        announced.addAll(names);
        Set<String> view = Collections.unmodifiableSet(names);
        channelListeners.forEach(listener -> listener.accept(view));
    }

    private static Connection connection() {
        ClientPacketListener listener = Minecraft.getInstance().getConnection();
        return listener == null ? null : listener.getConnection();
    }
}
