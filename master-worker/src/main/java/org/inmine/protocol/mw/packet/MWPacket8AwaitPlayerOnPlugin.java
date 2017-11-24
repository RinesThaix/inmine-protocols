package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * Created by RINES on 23.11.17.
 */
public class MWPacket8AwaitPlayerOnPlugin extends Packet {

    public UUID pluginSession;
    public UUID userSession;
    public String address;
    public String nickname;

    public MWPacket8AwaitPlayerOnPlugin() { }

    public MWPacket8AwaitPlayerOnPlugin(UUID pluginSession, UUID userSession, String address, String nickname) {
        this.pluginSession = pluginSession;
        this.userSession = userSession;
        this.address = address;
        this.nickname = nickname;
    }

    @Override
    public int getId() {
        return 8;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeUUID(this.pluginSession);
        buffer.writeUUID(this.userSession);
        buffer.writeString(this.address);
        buffer.writeStringNullable(this.nickname);
    }

    @Override
    public void read(Buffer buffer) {
        this.pluginSession = buffer.readUUID();
        this.userSession = buffer.readUUID();
        this.address = buffer.readString(50);
        this.nickname = buffer.readStringNullable(16);
    }

}
