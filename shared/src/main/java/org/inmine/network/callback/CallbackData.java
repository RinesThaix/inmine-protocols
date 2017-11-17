package org.inmine.network.callback;

import org.inmine.network.CallbackPacket;

import java.util.function.Consumer;

/**
 * Created by RINES on 16.11.17.
 */
public class CallbackData {
    
    private final Consumer<CallbackPacket> callback;
    
    private final long timeout;
    
    private final Runnable onTimeout;
    
    private final long initializationTime = System.currentTimeMillis();
    
    public CallbackData(Consumer<CallbackPacket> callback, long timeout, Runnable onTimeout) {
        this.callback = callback;
        this.timeout = timeout;
        this.onTimeout = onTimeout;
    }
    
    public Consumer<CallbackPacket> getCallback() {
        return callback;
    }
    
    public long getTimeout() {
        return timeout;
    }
    
    public Runnable getOnTimeout() {
        return onTimeout;
    }
    
    public long getInitializationTime() {
        return initializationTime;
    }
}
