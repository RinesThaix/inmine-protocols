package org.inmine.network.netty;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by RINES on 17.11.17.
 */
public class NamedThreadFactory implements ThreadFactory {
    private String name;
    private boolean daemon;
    private AtomicInteger counter;
    
    public NamedThreadFactory(String name, boolean daemon) {
        this.name = name;
        this.daemon = daemon;
        this.counter = new AtomicInteger(1);
    }
    
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(daemon);
        thread.setName(name + counter.getAndIncrement());
        return thread;
    }
}
