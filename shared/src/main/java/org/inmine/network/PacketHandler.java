package org.inmine.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by RINES on 17.11.17.
 */
public class PacketHandler {

    private final Map<Class<? extends Packet>, List<Consumer<Packet>>> handlers = new HashMap<>();
    protected Connection connection;
    private volatile long lastPacketSentTime;
    private volatile long lastPacketReceivedTime;

    public PacketHandler() {
        this.lastPacketSentTime = this.lastPacketReceivedTime = System.currentTimeMillis();
    }

    public void handle(Packet packet) {
        packetReceived();
        List<Consumer<Packet>> handlers = this.handlers.get(packet.getClass());
        if (handlers == null)
            return;
        for (Consumer<Packet> handler : handlers)
            handler.accept(packet);
    }

    public void packetReceived() {
        this.lastPacketReceivedTime = System.currentTimeMillis();
    }

    public void packetSent(Packet packet) {
        this.lastPacketSentTime = System.currentTimeMillis();
    }

    @SuppressWarnings("unchecked")
    public <T extends Packet> void addHandler(Class<T> packet, Consumer<T> handler) {
        this.handlers.computeIfAbsent(packet, list -> new ArrayList<>()).add((Consumer<Packet>) handler);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public <T extends Packet> void removeHandler(Class<T> packet, Consumer<T> handler) {
        List<Consumer<Packet>> list = this.handlers.get(packet);
        if (list == null)
            return;
        list.remove(handler);
    }

    public void setup(PacketProcessor processor) {
        processor.connection = this.connection;
        clearHandlers();
        processor.register();
    }

    public void clearHandlers() {
        handlers.clear();
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public long getLastPacketSentTime() {
        return lastPacketSentTime;
    }

    public long getLastPacketReceivedTime() {
        return lastPacketReceivedTime;
    }
}
