package org.inmine.network.packet;

import org.inmine.network.Packet;

import java.util.UUID;

/**
 * Created by RINES on 25.11.17.
 */
public interface ProxyPacket {

    Packet getPacket();

    UUID getSession();

}
