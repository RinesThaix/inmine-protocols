package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * Created by RINES on 23.11.17.
 */
public class MWPacket14UserSecretKey extends Packet {

    public String nickname;
    public String secretKey;
    public UUID pluginSession;

    public MWPacket14UserSecretKey() { }

    public MWPacket14UserSecretKey(String nickname, String secretKey, UUID pluginSession) {
        this.nickname = nickname;
        this.secretKey = secretKey;
        this.pluginSession = pluginSession;
    }

    @Override
    public int getId() {
        return 14;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeString(this.nickname);
        buffer.writeString(this.secretKey);
        buffer.writeUUID(this.pluginSession);
    }

    @Override
    public void read(Buffer buffer) {
        this.nickname = buffer.readString(16);
        this.secretKey = buffer.readString(16);
        this.pluginSession = buffer.readUUID();
    }

}
