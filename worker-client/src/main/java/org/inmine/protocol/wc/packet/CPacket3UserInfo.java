package org.inmine.protocol.wc.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * Created by RINES on 18.11.17.
 */
public class CPacket3UserInfo extends Packet {

    public int id;
    public String name;
    public int tag;

    public CPacket3UserInfo() { }

    public CPacket3UserInfo(int id, String name, int tag) {
        this.id = id;
        this.name = name;
        this.tag = tag;
    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeInt(this.id);
        buffer.writeString(this.name);
        buffer.writeVarInt(this.tag);
    }

    @Override
    public void read(Buffer buffer) {
        this.id = buffer.readInt();
        this.name = buffer.readString(100);
        this.tag = buffer.readVarInt();
    }
}
