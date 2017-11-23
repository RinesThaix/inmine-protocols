package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * Created by RINES on 23.11.17.
 */
public class PPacket8PlayerSecretKey extends Packet {

    public String nickname;
    public String secretKey;

    @Override
    public int getId() {
        return 8;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeString(this.nickname);
        buffer.writeString(this.secretKey);
    }

    @Override
    public void read(Buffer buffer) {
        this.nickname = buffer.readString(16);
        this.secretKey = buffer.readString(16);
    }

}
