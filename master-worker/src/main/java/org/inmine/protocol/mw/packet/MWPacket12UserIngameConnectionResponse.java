package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * Created by RINES on 23.11.17.
 */
public class MWPacket12UserIngameConnectionResponse extends Packet {

    public UUID userSession;
    public Status status;
    public UUID pluginSession;
    public String nickname;

    public MWPacket12UserIngameConnectionResponse() { }

    public MWPacket12UserIngameConnectionResponse(UUID userSession, Status status, UUID pluginSession, String nickname) {
        this.userSession = userSession;
        this.status = status;
        this.pluginSession = pluginSession;
        this.nickname = nickname;
    }

    @Override
    public int getId() {
        return 12;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeUUID(this.userSession);
        buffer.writeEnum(this.status);
        if (this.status == Status.OK) {
            buffer.writeUUID(this.pluginSession);
            buffer.writeString(this.nickname);
        }
    }

    @Override
    public void read(Buffer buffer) {
        this.userSession = buffer.readUUID();
        this.status = buffer.readEnum(Status.class);
        if (this.status == Status.OK) {
            this.pluginSession = buffer.readUUID();
            this.nickname = buffer.readString(16);
        }
    }

    public enum Status {
        OK,
        REQUIRES_APPROVAL,
        SERVER_IS_UNAVAILABLE,
        SERVER_LOST_CONNECTION,
        INTERNAL_ERROR
    }

}
