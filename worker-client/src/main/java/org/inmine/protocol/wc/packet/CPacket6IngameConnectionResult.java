package org.inmine.protocol.wc.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * Created by RINES on 23.11.17.
 */
public class CPacket6IngameConnectionResult extends Packet {

    public Status status;
    public String secretKey;
    public String nickname;
    public int errorCode;

    public CPacket6IngameConnectionResult() { }

    public CPacket6IngameConnectionResult(Status status, String secretKey, String nickname, int errorCode) {
        this.status = status;
        this.secretKey = secretKey;
        this.nickname = nickname;
        this.errorCode = errorCode;
    }

    @Override
    public int getId() {
        return 6;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeEnum(this.status);
        if (this.status == Status.SUCCESS) {
            buffer.writeString(this.nickname);
        } else if (this.status == Status.ENTER_APPROVAL_KEY) {
            buffer.writeString(this.secretKey);
        } else {
            buffer.writeVarInt(this.errorCode);
        }
    }

    @Override
    public void read(Buffer buffer) {
        this.status = buffer.readEnum(Status.class);
        if (this.status == Status.SUCCESS) {
            this.nickname = buffer.readString(16);
        } else if (this.status == Status.ENTER_APPROVAL_KEY) {
            this.secretKey = buffer.readString(16);
        } else {
            this.errorCode = buffer.readVarInt();
        }
    }

    public enum Status {
        SUCCESS,
        ENTER_APPROVAL_KEY,
        ERROR
    }

}
