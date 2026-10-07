package me.erano.com.fabric.V1_21_11.network;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import me.erano.com.common.network.Channels;
import me.erano.com.common.network.ClientNetwork;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.C2SPlayChannelEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * {@link ClientNetwork} on Fabric API's play networking; registered in META-INF/services.
 * Fabric API for 1.21.11 still uses the C2S / S2C names (26.x: serverbound / clientbound).
 */
public final class FabricClientNetwork implements ClientNetwork {

    private final Map<String, CustomPacketPayload.Type<RawPayload>> outgoing = new ConcurrentHashMap<>();

    @Override
    public void registerOutgoing(String channel) {
        CustomPacketPayload.Type<RawPayload> type = RawPayload.type(Channels.validate(channel));
        PayloadTypeRegistry.playC2S().register(type, RawPayload.codec(type));
        outgoing.put(channel, type);
    }

    @Override
    public void registerIncoming(String channel, Consumer<byte[]> handler) {
        CustomPacketPayload.Type<RawPayload> type = RawPayload.type(Channels.validate(channel));
        PayloadTypeRegistry.playS2C().register(type, RawPayload.codec(type));
        // Fabric runs play payload receivers on the client thread.
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> handler.accept(payload.data()));
    }

    @Override
    public boolean canSend(String channel) {
        return ClientPlayNetworking.canSend(Identifier.parse(channel));
    }

    @Override
    public void send(String channel, byte[] data) {
        CustomPacketPayload.Type<RawPayload> type = outgoing.get(channel);
        if (type == null) {
            throw new IllegalStateException("Channel " + channel + " isn't registered as outgoing");
        }
        ClientPlayNetworking.send(new RawPayload(type, data));
    }

    @Override
    public void onJoin(Runnable listener) {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> listener.run());
    }

    @Override
    public void onDisconnect(Runnable listener) {
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> listener.run());
    }

    @Override
    public void onServerChannelsRegistered(Consumer<Set<String>> listener) {
        C2SPlayChannelEvents.REGISTER.register((handler, sender, client, channels) ->
                listener.accept(channels.stream().map(Identifier::toString).collect(Collectors.toSet())));
    }
}
