package org.inmine.network.callback;

import org.inmine.network.NetworkClient;
import org.inmine.network.Packet;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Created by RINES on 16.11.17.
 */
public abstract class AbstractNetworkClient extends CallbackHandler implements NetworkClient {
    
    @Override
    public <T extends CallbackPacket> void sendPacket(T packet, Consumer<CallbackPacket> callback, long timeout, Runnable onTimeout) {
        super.processCallbackPacket(packet, callback, timeout, onTimeout);
        sendPacket(packet);
    }

}
