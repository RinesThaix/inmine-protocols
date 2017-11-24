package org.inmine.protocol.wc.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * Created by RINES on 23.11.17.
 */
public class CPacket7IngameDisconnect extends Packet {

    @Override
    public int getId() {
        return 7;
    }

    @Override
    public void write(Buffer buffer) {

    }

    @Override
    public void read(Buffer buffer) {

    }

}
