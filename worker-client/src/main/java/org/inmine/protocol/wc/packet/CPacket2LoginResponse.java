package org.inmine.protocol.wc.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * Created by RINES on 18.11.17.
 */
public class CPacket2LoginResponse extends Packet {

    public Status status;
    public int userId;
    public UUID session;

    public CPacket2LoginResponse() { }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeEnum(this.status);
        if (this.status == Status.OK) {
            buffer.writeInt(this.userId);
            buffer.writeUUID(this.session);
        }
    }

    @Override
    public void read(Buffer buffer) {
        this.status = buffer.readEnum(Status.class);
        if (this.status == Status.OK) {
            this.userId = buffer.readInt();
            this.session = buffer.readUUID();
        }
    }

    public enum Status {
        OK,
        INVALID_CREDENTIALS,
        ACCOUNT_DOESNT_EXIST
    }
}
