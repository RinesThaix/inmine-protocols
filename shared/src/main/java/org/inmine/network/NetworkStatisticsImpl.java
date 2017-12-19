package org.inmine.network;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xtrafrancyz
 */
public class NetworkStatisticsImpl implements NetworkStatistics {
    protected AtomicLong sentBytes = new AtomicLong();
    protected AtomicLong receivedBytes = new AtomicLong();
    protected AtomicLong sentPackets = new AtomicLong();
    protected AtomicLong receivedPackets = new AtomicLong();
    

    public AtomicLong sentBytes() {
        return sentBytes;
    }

    public AtomicLong receivedBytes() {
        return receivedBytes;
    }

    public AtomicLong sentPackets() {
        return sentPackets;
    }

    public AtomicLong receivedPackets() {
        return receivedPackets;
    }

    @Override
    public long getSentBytes() {
        return sentBytes.get();
    }

    @Override
    public long getReceivedBytes() {
        return receivedBytes.get();
    }

    @Override
    public long getSentPackets() {
        return sentPackets.get();
    }

    @Override
    public long getReceivedPackets() {
        return receivedPackets.get();
    }
}
