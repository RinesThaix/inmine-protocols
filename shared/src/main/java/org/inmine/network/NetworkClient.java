package org.inmine.network;

import org.inmine.network.callback.CallbackPacket;

import java.util.function.Consumer;

/**
 * Created by RINES on 16.11.17.
 */
public interface NetworkClient {
    
    void connect(String address, int port);
    
    void disconnect();
    
    PacketRegistry getPacketRegistry();
    
    Connection getConnection();
    
    void sendPacket(Packet packet);
    
    default <T extends CallbackPacket> void sendPacket(T packet, Consumer<CallbackPacket> callback) {
        sendPacket(packet, callback, 3000L);
    }
    
    default <T extends CallbackPacket> void sendPacket(T packet, Consumer<CallbackPacket> callback, long timeout) {
        sendPacket(packet, callback, timeout, null);
    }
    
    <T extends CallbackPacket> void sendPacket(T packet, Consumer<CallbackPacket> callback, long timeout, Runnable onTimeout);
    
    void onConnected();
    
    void onDisconnected();
    
}
