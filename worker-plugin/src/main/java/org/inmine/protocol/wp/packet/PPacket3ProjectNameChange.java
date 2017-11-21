package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * Created by RINES on 21.11.17.
 */
public class PPacket3ProjectNameChange extends Packet {

    public int projectId;
    public String name;

    @Override
    public int getId() {
        return 3;
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
