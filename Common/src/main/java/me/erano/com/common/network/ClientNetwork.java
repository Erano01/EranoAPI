package me.erano.com.common.network;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Raw plugin messaging from a client mod to the server it is connected to. Payloads are opaque bytes,
 * written as is, so every platform (Spigot, Fabric, Forge) sees the same wire format.
 *
 * <p>Register channels while your mod initializes, before any connection is made. Handlers and
 * listeners run on the client thread.
 */
public interface ClientNetwork {

    /** The implementation of the platform this mod runs on (EranoAPI-Fabric, EranoAPI-Forge). */
    static ClientNetwork get() {
        return ClientNetworkHolder.INSTANCE;
    }

    /** Declares a channel this mod sends on. */
    void registerOutgoing(String channel);

    /** Declares a channel this mod receives on. */
    void registerIncoming(String channel, Consumer<byte[]> handler);

    /** Whether the server announced {@code channel}, i.e. it has a plugin / mod listening on it. */
    boolean canSend(String channel);

    /** @throws IllegalStateException if {@code channel} wasn't registered as outgoing */
    void send(String channel, byte[] data);

    /** The player joined a world on a server. */
    void onJoin(Runnable listener);

    /** The connection to the server closed. */
    void onDisconnect(Runnable listener);

    /**
     * The server announced channels (vanilla {@code minecraft:register}); can happen before or after
     * {@link #onJoin}.
     */
    void onServerChannelsRegistered(Consumer<Set<String>> listener);
}

/** Lazy, so it doesn't matter whether the platform mod or the calling mod initializes first. */
final class ClientNetworkHolder {

    static final ClientNetwork INSTANCE = load();

    private ClientNetworkHolder() {
    }

    private static ClientNetwork load() {
        Iterator<ClientNetwork> implementations = ServiceLoader.load(ClientNetwork.class, ClientNetwork.class.getClassLoader()).iterator();
        if (!implementations.hasNext()) {
            throw new IllegalStateException("No ClientNetwork implementation: install / include EranoAPI-Fabric or EranoAPI-Forge for this Minecraft version");
        }
        return implementations.next();
    }
}
