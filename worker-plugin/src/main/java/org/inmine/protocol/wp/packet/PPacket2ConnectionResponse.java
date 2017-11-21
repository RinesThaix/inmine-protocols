package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * Created by RINES on 21.11.17.
 */
public class PPacket2ConnectionResponse extends Packet {

    public Status status;
    public int projectId;
    public UUID session;

    public PPacket2ConnectionResponse() { }

    public PPacket2ConnectionResponse(Status status) {
        this.status = status;
    }

    public PPacket2ConnectionResponse(Status status, int projectId, UUID session) {
        this.status = status;
        this.projectId = projectId;
        this.session = session;
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeEnum(this.status);
        if (this.status == Status.OK) {
            buffer.writeVarInt(this.projectId);
            buffer.writeLong(this.session.getMostSignificantBits());
            buffer.writeLong(this.session.getLeastSignificantBits());
        }
    }

    @Override
    public void read(Buffer buffer) {
        this.status = buffer.readEnum(Status.class);
        if (this.status == Status.OK) {
            this.projectId = buffer.readVarInt();
            this.session = new UUID(buffer.readLong(), buffer.readLong());
        }
    }

    public enum Status {
        OK,
        UNKNOWN_PROJECT_ID,
        INVALID_SECRET_KEY
    }

}
