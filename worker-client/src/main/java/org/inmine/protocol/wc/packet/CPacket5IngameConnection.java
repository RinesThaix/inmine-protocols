package org.inmine.protocol.wc.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * Created by RINES on 23.11.17.
 */
public class CPacket5IngameConnection extends Packet {

    public String address;

    @Override
    public int getId() {
        return 5;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeString(this.address);
    }

    @Override
    public void read(Buffer buffer) {
        this.address = buffer.readString(16);
    }

}
