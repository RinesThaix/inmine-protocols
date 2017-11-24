package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * Created by RINES on 21.11.17.
 */
public class MWPacket6ProjectSecretKeyChanged extends Packet {

    public int projectId;
    public String secretKey;

    public MWPacket6ProjectSecretKeyChanged() { }

    public MWPacket6ProjectSecretKeyChanged(int projectId, String secretKey) {
        this.projectId = projectId;
        this.secretKey = secretKey;
    }

    @Override
    public int getId() {
        return 8;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeVarInt(this.projectId);
        buffer.writeString(this.secretKey);
    }

    @Override
    public void read(Buffer buffer) {
        this.projectId = buffer.readVarInt();
        this.secretKey = buffer.readString(64);
    }
}
