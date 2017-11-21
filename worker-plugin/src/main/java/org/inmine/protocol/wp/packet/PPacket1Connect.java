package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;
import org.inmine.protocol.wp.ServerType;

/**
 * Created by RINES on 21.11.17.
 */
public class PPacket1Connect extends Packet {

    public ServerType serverType;
    public int projectId;
    public String secretKey;

    public PPacket1Connect() { }

    public PPacket1Connect(ServerType serverType, int projectId, String secretKey) {
        this.serverType = serverType;
        this.projectId = projectId;
        this.secretKey = secretKey;
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeEnum(this.serverType);
        buffer.writeVarInt(this.projectId);
        buffer.writeString(this.secretKey);
    }

    @Override
    public void read(Buffer buffer) {
        this.serverType = buffer.readEnum(ServerType.class);
        this.projectId = buffer.readVarInt();
        this.secretKey = buffer.readString(64);
    }
}
