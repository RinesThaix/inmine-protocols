package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;
import org.inmine.protocol.wp.ServerType;

/**
 * Created by RINES on 21.11.17.
 */
public class PPacket1Connect extends Packet {

    public ServerType serverType;
    public int subProtocol;
    public String secretKey;

    public PPacket1Connect() { }

    public PPacket1Connect(ServerType serverType, int subProtocol, String secretKey) {
        this.serverType = serverType;
        this.subProtocol = subProtocol;
        this.secretKey = secretKey;
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeEnum(this.serverType);
        buffer.writeVarInt(this.subProtocol);
        buffer.writeString(this.secretKey);
    }

    @Override
    public void read(Buffer buffer) {
        this.serverType = buffer.readEnum(ServerType.class);
        this.subProtocol = buffer.readVarInt();
        this.secretKey = buffer.readString(64);
    }
}
