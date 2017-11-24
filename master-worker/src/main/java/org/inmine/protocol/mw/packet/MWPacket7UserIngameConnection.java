package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * Created by RINES on 23.11.17.
 */
public class MWPacket7UserIngameConnection extends Packet {

    public UUID userSession;
    public String address;

    public MWPacket7UserIngameConnection() { }

    public MWPacket7UserIngameConnection(UUID userSession, String address) {
        this.userSession = userSession;
        this.address = address;
    }

    @Override
    public int getId() {
        return 7;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeUUID(this.userSession);
        buffer.writeString(this.address);
    }

    @Override
    public void read(Buffer buffer) {
        this.userSession = buffer.readUUID();
        this.address = buffer.readString(50);
    }
}
