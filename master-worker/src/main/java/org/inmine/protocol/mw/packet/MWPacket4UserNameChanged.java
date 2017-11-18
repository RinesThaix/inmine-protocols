package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * Created by RINES on 19.11.17.
 */
public class MWPacket4UserNameChanged extends Packet {

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
