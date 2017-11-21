package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * Created by RINES on 21.11.17.
 */
public class MWPacket7ProjectNameChanged extends Packet {

    public int projectId;
    public String name;

    public MWPacket7ProjectNameChanged() { }

    public MWPacket7ProjectNameChanged(int projectId, String name) {
        this.projectId = projectId;
        this.name = name;
    }

    @Override
    public int getId() {
        return 7;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeVarInt(this.projectId);
        buffer.writeString(this.name);
    }

    @Override
    public void read(Buffer buffer) {
        this.projectId = buffer.readVarInt();
        this.name = buffer.readString(100);
    }

}
