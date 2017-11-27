package org.inmine.protocol.wc.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * Created by RINES on 18.11.17.
 */
public class CPacket2LoginResponse extends Packet {

    public boolean success;
    public int userId;
    public UUID session;

    public CPacket2LoginResponse() { }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeBoolean(this.success);
        if (this.success) {
            buffer.writeInt(this.userId);
            buffer.writeUUID(this.session);
        }
    }

    @Override
    public void read(Buffer buffer) {
        this.success = buffer.readBoolean();
        if (this.success) {
            this.userId = buffer.readInt();
            this.session = buffer.readUUID();
        }
    }
}
