package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * Created by RINES on 23.11.17.
 */
public class MWPacket11AwaitPlayerOnPlugin extends Packet {

    public int projectId;
    public UUID pluginSession;
    public String address;
    public String nickname;

    public MWPacket11AwaitPlayerOnPlugin() { }

    public MWPacket11AwaitPlayerOnPlugin(int projectId, UUID pluginSession, String address, String nickname) {
        this.projectId = projectId;
        this.pluginSession = pluginSession;
        this.address = address;
        this.nickname = nickname;
    }

    @Override
    public int getId() {
        return 11;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeVarInt(this.projectId);
        buffer.writeUUID(this.pluginSession);
        buffer.writeString(this.address);
        buffer.writeStringNullable(this.nickname);
    }

    @Override
    public void read(Buffer buffer) {
        this.projectId = buffer.readVarInt();
        this.pluginSession = buffer.readUUID();
        this.address = buffer.readString(16);
        this.nickname = buffer.readStringNullable(16);
    }

}
