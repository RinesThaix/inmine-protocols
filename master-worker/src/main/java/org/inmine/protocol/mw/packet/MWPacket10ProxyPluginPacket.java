package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;
import org.inmine.network.PacketRegistry;
import org.inmine.network.packet.ProxyPacket;
import org.inmine.protocol.mw.MWPacketRegistry;

import java.util.UUID;

/**
 * Created by RINES on 24.11.17.
 */
public class MWPacket10ProxyPluginPacket extends Packet implements ProxyPacket {

    public UUID session;
    public Packet packet;

    public MWPacket10ProxyPluginPacket() { }

    public MWPacket10ProxyPluginPacket(UUID session, Packet packet) {
        this.session = session;
        this.packet = packet;
    }

    @Override
    public int getId() {
        return 10;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeUUID(this.session);
        buffer.writeVarInt(this.packet.getId());
        this.packet.write(buffer);
    }

    @Override
    public void read(Buffer buffer) {
        this.session = buffer.readUUID();
        this.packet = getRegistry().constructPacket(buffer.readVarInt());
        this.packet.read(buffer);
    }

    private static PacketRegistry getRegistry() {
        return MWPacketRegistry.instance().getPluginPacketRegistry();
    }

    @Override
    public Packet getPacket() {
        return this.packet;
    }

    @Override
    public UUID getSession() {
        return this.session;
    }
}
