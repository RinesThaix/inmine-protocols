package org.inmine.network.callback;

import org.inmine.network.Packet;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by RINES on 17.11.17.
 */
public abstract class CallbackHandler {

    protected final Logger logger;

    private AtomicInteger callbackIdentifier = new AtomicInteger(0);
    private Map<Integer, CallbackData> callbacks = new ConcurrentHashMap<>();

    protected CallbackHandler(Logger logger) {
        this.logger = logger;
    }

    protected void callbackTick() {
        try {
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
                        logger.log(Level.WARNING, "Can not process onTimeout for callback with id " + entry.getKey(), ex);
                    }
                }
            }
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Exception in callback tick", ex);
        }
    }

    protected void callCallbacksTimeouts() {
        this.callbacks.values().forEach(cdata -> {
            if (cdata.getOnTimeout() != null)
                cdata.getOnTimeout().run();
        });
    }

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
                logger.log(Level.WARNING, "Can not process callback for " + packet.getClass().getSimpleName(), ex);
            }
        }
    }

    public <T extends CallbackPacket> void registerCallback(T packet, Consumer<CallbackPacket> callback, long timeout, Runnable onTimeout) {
        int id = this.callbackIdentifier.updateAndGet(l -> l == Integer.MAX_VALUE ? 1 : l + 1);
        packet.callbackId = id;
        this.callbacks.put(id, new CallbackData(callback, timeout, onTimeout));
    }

}
