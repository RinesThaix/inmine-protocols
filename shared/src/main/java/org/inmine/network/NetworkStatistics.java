package org.inmine.network;

/**
 * @author xtrafrancyz
 */
public interface NetworkStatistics {
    long getSentBytes();

    long getReceivedBytes();

    long getSentPackets();

    long getReceivedPackets();
}
