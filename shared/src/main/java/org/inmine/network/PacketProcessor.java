package org.inmine.network;

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

}
