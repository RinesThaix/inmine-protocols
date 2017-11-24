package org.inmine.network;

import org.inmine.network.packet.ProxyPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by RINES on 18.11.17.
 */
public abstract class PacketProcessor {

    protected Connection connection;

    protected abstract void register();

    protected <T extends Packet> void addHandler(Class<T> packet, Consumer<T> handler) {
        this.connection.getHandler().addHandler(packet, handler);
    }

    public static class InternalProcessor {

        private final Map<Class<? extends Packet>, List<BiConsumer<UUID, Packet>>> handlers = new HashMap<>();

        @SuppressWarnings("unchecked")
        public <T extends Packet> void addHandler(Class<T> packet, BiConsumer<UUID, T> handler) {
            this.handlers.computeIfAbsent(packet, list -> new ArrayList<>()).add((BiConsumer<UUID, Packet>) handler);
        }

        public void handle(ProxyPacket proxied) {
            Packet packet = proxied.getPacket();
            UUID session = proxied.getSession();
            List<BiConsumer<UUID, Packet>> handlers = this.handlers.get(packet.getClass());
            if (handlers == null)
                return;
            handlers.forEach(handler -> handler.accept(session, packet));
        }

    }

}
