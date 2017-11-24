package org.inmine.protocol.wc.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * Created by RINES on 18.11.17.
 */
public class CPacket4ChangeName extends Packet {

    public int userId;
    public String name;

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeInt(this.userId);
        buffer.writeString(this.name);
    }

    @Override
    public void read(Buffer buffer) {
        this.userId = buffer.readInt();
        this.name = buffer.readString(100);
    }
}
