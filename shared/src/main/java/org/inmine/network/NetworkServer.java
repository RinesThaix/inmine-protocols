package org.inmine.network;

import org.inmine.network.callback.CallbackPacket;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Created by RINES on 16.11.17.
 */
public interface NetworkServer {
    
    void start(String address, int port);
    
    void stop();
    
    String getAddress();
    
    int getBindPort();
    
    Collection<? extends Connection> getConnections();
    
    PacketRegistry getPacketRegistry();
    
    void sendPacket(Connection connection, Packet packet);
    
    default <T extends CallbackPacket> void sendPacket(Connection connection, T packet, Consumer<CallbackPacket> callback) {
        sendPacket(connection, packet, callback, 3000L);
    }
    
    default <T extends CallbackPacket> void sendPacket(Connection connection, T packet, Consumer<CallbackPacket> callback, long timeout) {
        sendPacket(connection, packet, callback, timeout, null);
    }
    
    <T extends CallbackPacket> void sendPacket(Connection connection, T packet, Consumer<CallbackPacket> callback, long timeout, Runnable onTimeout);
    
    default void sendPacket(Collection<Connection> connections, Packet packet) {
        connections.forEach(connection -> sendPacket(connection, packet));
    }
    
    void onNewConnection(Connection connection);
    
    void onDisconnecting(Connection connection);
    
}
