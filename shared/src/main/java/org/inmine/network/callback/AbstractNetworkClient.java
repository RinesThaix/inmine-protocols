package org.inmine.network.callback;

import org.inmine.network.CallbackPacket;
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
public abstract class AbstractNetworkClient implements NetworkClient {
    
    private AtomicInteger callbackIdentifier = new AtomicInteger(0);
    private Map<Integer, CallbackData> callbacks = new ConcurrentHashMap<>();
    
    protected void callbackTick() {
        long current = System.currentTimeMillis();
        Iterator<Map.Entry<Integer, CallbackData>> iterator = this.callbacks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, CallbackData> entry = iterator.next();
            CallbackData data = entry.getValue();
            if (current - data.getInitializationTime() > data.getTimeout()) {
                iterator.remove();
                try {
                    if (data.getOnTimeout() == null)
                        return;
                    data.getOnTimeout().run();
                } catch (Exception ex) {
                    new Exception("Can not process onTimeout for packet with id " + entry.getKey(), ex).printStackTrace();
                }
            }
        }
    }
    
    @Override
    public <T extends CallbackPacket> void sendPacket(T packet, Consumer<CallbackPacket> callback, long timeout, Runnable onTimeout) {
        int id = this.callbackIdentifier.updateAndGet(l -> l == Integer.MAX_VALUE ? 1 : l + 1);
        packet.callbackId = id;
        this.callbacks.put(id, new CallbackData(callback, timeout, onTimeout));
        sendPacket(packet);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Packet> void onPacketPreReceived(T packet) {
        if (packet instanceof CallbackPacket) {
            int id = ((CallbackPacket) packet).callbackId;
            if (id == 0)
                return;
            CallbackData callback = this.callbacks.remove(id);
            if (callback == null)
                return;
            try {
                callback.getCallback().accept((CallbackPacket) packet);
            } catch (Exception ex) {
                new Exception("Can not process callback for " + packet.getClass().getSimpleName(), ex).printStackTrace();
            }
        }
    }
}
