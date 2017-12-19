package org.inmine.network;

import java.util.Collection;
import java.util.function.BiConsumer;

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

    void onNewConnection(Connection connection);

    void onDisconnecting(Connection connection);

    void setPacketReceivedListener(BiConsumer<Connection, Packet> listener);

    void setPacketSentListener(BiConsumer<Connection, Packet> listener);

    NetworkStatistics getStatistics();

}
