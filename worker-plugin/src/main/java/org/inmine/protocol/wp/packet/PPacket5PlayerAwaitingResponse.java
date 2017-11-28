package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.callback.CallbackPacket;

/**
 * Created by RINES on 23.11.17.
 */
public class PPacket5PlayerAwaitingResponse extends CallbackPacket {

    public Status status;
    public String nickname;

    public PPacket5PlayerAwaitingResponse() { }

    public PPacket5PlayerAwaitingResponse(Status status, String nickname) {
        this.status = status;
        this.nickname = nickname;
    }

    @Override
    public int getId() {
        return 5;
    }

    @Override
    public void write(Buffer buffer) {
        super.write(buffer);
        buffer.writeEnum(this.status);
        if (this.status == Status.OK)
            buffer.writeString(this.nickname);
    }

    @Override
    public void read(Buffer buffer) {
        super.read(buffer);
        this.status = buffer.readEnum(Status.class);
        if (this.status == Status.OK)
            this.nickname = buffer.readString(40);
    }

    public enum Status {
        OK,
        SOMETHING_IS_WRONG
    }

}
