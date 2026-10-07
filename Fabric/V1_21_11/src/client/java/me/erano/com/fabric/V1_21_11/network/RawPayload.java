package me.erano.com.fabric.V1_21_11.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Opaque payload: the codec copies bytes in and out untouched, so Fabric's bytes are identical to
 * what Spigot and Forge see.
 */
public record RawPayload(Type<RawPayload> type, byte[] data) implements CustomPacketPayload {

    public static Type<RawPayload> type(String channel) {
        return new Type<>(Identifier.parse(channel));
    }

    public static StreamCodec<FriendlyByteBuf, RawPayload> codec(Type<RawPayload> type) {
        return StreamCodec.of(
                (buf, payload) -> buf.writeBytes(payload.data()),
                buf -> {
                    byte[] data = new byte[buf.readableBytes()];
                    buf.readBytes(data);
                    return new RawPayload(type, data);
                });
    }
}
