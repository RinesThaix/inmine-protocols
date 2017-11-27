package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;
import org.inmine.plugin.PluginType;

import java.util.UUID;

/**
 * Created by RINES on 21.11.17.
 */
public class MWPacket4PluginSessionCreate extends Packet {

    public PluginType pluginType;
    public int projectId;
    public UUID session;

    public MWPacket4PluginSessionCreate() { }

    public MWPacket4PluginSessionCreate(PluginType pluginType, int projectId, UUID session) {
        this.pluginType = pluginType;
        this.projectId = projectId;
        this.session = session;
    }

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeEnum(this.pluginType);
        buffer.writeVarInt(this.projectId);
        buffer.writeUUID(this.session);
    }

    @Override
    public void read(Buffer buffer) {
        this.pluginType = buffer.readEnum(PluginType.class);
        this.projectId = buffer.readVarInt();
        this.session = buffer.readUUID();
    }

}
