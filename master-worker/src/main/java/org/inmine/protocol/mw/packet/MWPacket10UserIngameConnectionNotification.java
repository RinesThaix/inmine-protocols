package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * Created by RINES on 23.11.17.
 */
public class MWPacket10UserIngameConnectionNotification extends Packet {

    public UUID userSession;
    public Status status;
    public String secretKey;
    public String nickname;
    public MWPacket9UserIngameConnectionResponse.Status error;

    public MWPacket10UserIngameConnectionNotification() { }

    public MWPacket10UserIngameConnectionNotification(UUID userSession, Status status, String secretKey, String nickname, MWPacket9UserIngameConnectionResponse.Status error) {
        this.userSession = userSession;
        this.status = status;
        this.secretKey = secretKey;
        this.nickname = nickname;
        this.error = error;
    }

    @Override
    public int getId() {
        return 12;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeUUID(this.userSession);
        buffer.writeEnum(this.status);
        if (this.status == Status.SUCCESS)
            buffer.writeString(this.nickname);
        else if (this.status == Status.ENTER_APPROVAL_KEY)
            buffer.writeString(this.secretKey);
        else if (this.status == Status.ERROR)
            buffer.writeEnum(this.error);
    }

    @Override
    public void read(Buffer buffer) {
        this.userSession = buffer.readUUID();
        this.status = buffer.readEnum(Status.class);
        if (this.status == Status.SUCCESS)
            this.nickname = buffer.readString(16);
        else if (this.status == Status.ENTER_APPROVAL_KEY)
            this.secretKey = buffer.readString(16);
        else if (this.status == Status.ERROR)
            this.error = buffer.readEnum(MWPacket9UserIngameConnectionResponse.Status.class);
    }

    public enum Status {
        SUCCESS,
        ENTER_APPROVAL_KEY,
        ERROR
    }

}
