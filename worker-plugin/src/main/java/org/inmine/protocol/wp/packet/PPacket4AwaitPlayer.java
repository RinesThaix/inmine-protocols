package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.callback.CallbackPacket;

/**
 * Created by RINES on 23.11.17.
 */
public class PPacket4AwaitPlayer extends CallbackPacket {

    public String address;
    public String nickname;

    public PPacket4AwaitPlayer() { }

    public PPacket4AwaitPlayer(String address, String nickname) {
        this.address = address;
        this.nickname = nickname;
    }

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public void write(Buffer buffer) {
        super.write(buffer);
        buffer.writeString(this.address);
        buffer.writeStringNullable(this.nickname);
    }

    @Override
    public void read(Buffer buffer) {
        super.read(buffer);
        this.address = buffer.readString(16);
        this.nickname = buffer.readStringNullable(16);
    }

}
