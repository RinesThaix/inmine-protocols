package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * Created by RINES on 21.11.17.
 */
public class MWPacket5PluginSessionRemove extends Packet {

    public int projectId;
    public UUID session;

    public MWPacket5PluginSessionRemove() { }

    public MWPacket5PluginSessionRemove(int projectId, UUID session) {
        this.projectId = projectId;
        this.session = session;
    }

    @Override
    public int getId() {
        return 5;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeVarInt(this.projectId);
        buffer.writeUUID(this.session);
    }

    @Override
    public void read(Buffer buffer) {
        this.projectId = buffer.readVarInt();
        this.session = buffer.readUUID();
    }

}
