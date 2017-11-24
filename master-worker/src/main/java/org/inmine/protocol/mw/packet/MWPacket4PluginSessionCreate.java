package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;
import org.inmine.protocol.wp.ServerType;

import java.util.UUID;

/**
 * Created by RINES on 21.11.17.
 */
public class MWPacket4PluginSessionCreate extends Packet {

    public ServerType serverType;
    public int projectId;
    public UUID session;

    public MWPacket4PluginSessionCreate() { }

    public MWPacket4PluginSessionCreate(ServerType serverType, int projectId, UUID session) {
        this.serverType = serverType;
        this.projectId = projectId;
        this.session = session;
    }

    @Override
    public int getId() {
        return 5;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeEnum(this.serverType);
        buffer.writeVarInt(this.projectId);
        buffer.writeUUID(this.session);
    }

    @Override
    public void read(Buffer buffer) {
        this.serverType = buffer.readEnum(ServerType.class);
        this.projectId = buffer.readVarInt();
        this.session = buffer.readUUID();
    }

}
