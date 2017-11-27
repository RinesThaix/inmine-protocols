package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * @author xtrafrancyz
 */
public class MWPacket12ProxyToMaster extends PacketContainer {
    public UUID session;
    public Protocol protocol;

    public MWPacket12ProxyToMaster() { }

    public MWPacket12ProxyToMaster(UUID session, Packet packet, Protocol protocol) {
        super(packet);
        this.session = session;
        this.protocol = protocol;
    }

    public MWPacket12ProxyToMaster(Packet packet, Protocol protocol) {
        super(packet);
        this.protocol = protocol;
    }

    @Override
    public int getId() {
        return 12;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeEnum(protocol);
        buffer.writeUUIDNullable(session);
        super.write(buffer);
    }

    @Override
    public void read(Buffer buffer) {
        protocol = buffer.readEnum(Protocol.class);
        session = buffer.readUUIDNullable();
        super.read(buffer);
    }

    public enum Protocol {
        WORKER_PLUGIN,
        WORKER_CLIENT
    }
}
