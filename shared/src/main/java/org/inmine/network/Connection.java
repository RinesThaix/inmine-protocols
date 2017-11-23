package org.inmine.network;

import org.inmine.network.callback.CallbackPacket;
import org.inmine.network.packet.SPacketDisconnect;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

/**
 * Created by RINES on 16.11.17.
 */
public interface Connection {
    PacketHandler getHandler();
    
    void sendPacket(Packet packet);
    
    default <T extends CallbackPacket> void sendPacket(T packet, Consumer<CallbackPacket> callback) {
        sendPacket(packet, callback, 3000L);
    }
    
    default <T extends CallbackPacket> void sendPacket(T packet, Consumer<CallbackPacket> callback, long timeout) {
        sendPacket(packet, callback, timeout, null);
    }
    
    <T extends CallbackPacket> void sendPacket(T packet, Consumer<CallbackPacket> callback, long timeout, Runnable onTimeout);
    
    void disconnect();

    InetSocketAddress getRemoteAddress();
    
    default void disconnect(String reason) {
        sendPacket(new SPacketDisconnect(reason));
        disconnect();
    }
}
