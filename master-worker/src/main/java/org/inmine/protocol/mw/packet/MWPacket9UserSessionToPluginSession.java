package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * Created by RINES on 23.11.17.
 */
public class MWPacket9UserSessionToPluginSession extends Packet {

    public int userId;
    public int projectId;
    public UUID userSession;
    public UUID pluginSession;

    public MWPacket9UserSessionToPluginSession() { }

    public MWPacket9UserSessionToPluginSession(int userId, int projectId, UUID userSession, UUID pluginSession) {
        this.userId = userId;
        this.projectId = projectId;
        this.userSession = userSession;
        this.pluginSession = pluginSession;
    }

    @Override
    public int getId() {
        return 9;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeInt(this.userId);
        buffer.writeVarInt(this.projectId);
        buffer.writeUUID(this.userSession);
        buffer.writeUUID(this.pluginSession);
    }

    @Override
    public void read(Buffer buffer) {
        this.userId = buffer.readInt();
        this.projectId = buffer.readVarInt();
        this.userSession = buffer.readUUID();
        this.pluginSession = buffer.readUUID();
    }

}
