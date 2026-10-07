package me.erano.com.common.network;

import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * Raw plugin messaging from the server to its players. Payloads are opaque bytes, written as is, so
 * every platform sees the same wire format. Players are identified by UUID to stay platform agnostic.
 *
 * <p>Handlers run on the server thread.
 */
public interface ServerNetwork {

    /** Declares a channel the server sends on. */
    void registerOutgoing(String channel);

    /** Declares a channel the server receives on; the handler gets the sending player's UUID. */
    void registerIncoming(String channel, BiConsumer<UUID, byte[]> handler);

    /** Whether the player is online and its client announced {@code channel}. */
    boolean canSend(UUID player, String channel);

    /**
     * @throws IllegalStateException if {@code channel} wasn't registered as outgoing or the player
     *                               isn't online
     */
    void send(UUID player, String channel, byte[] data);

    /** Unregisters every channel registered through this instance. */
    void close();
}
