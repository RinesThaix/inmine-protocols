package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * Created by RINES on 23.11.17.
 */
public class PPacket8PlayerSecretKey extends Packet {

    public String nickname;
    public String secretKey;

    public PPacket8PlayerSecretKey() { }

    public PPacket8PlayerSecretKey(String nickname, String secretKey) {
        this.nickname = nickname;
        this.secretKey = secretKey;
    }

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
        this.nickname = buffer.readString(NICKNAME_SIZE);
        this.secretKey = buffer.readString(16);
    }

}
