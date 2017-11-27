package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * @author xtrafrancyz
 */
public class MWPacket10Proxy extends PacketContainer {
    public UUID session;
    public Destination destination;

    public MWPacket10Proxy() { }

    public MWPacket10Proxy(UUID session, Destination destination, Packet packet) {
        super(packet);
        this.session = session;
        this.destination = destination;
    }

    @Override
    public int getId() {
        return 10;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeUUID(session);
        buffer.writeEnum(destination);
        super.write(buffer);
    }

    @Override
    public void read(Buffer buffer) {
        session = buffer.readUUID();
        destination = buffer.readEnum(Destination.class);
        super.read(buffer);
    }

    public enum Destination {
        CLIENT,
        PLUGIN
    }
}
