package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * Created by RINES on 23.11.17.
 */
public class MWPacket10UserIngameConnection extends Packet {

    public int userId;
    public UUID userSession;
    public String address;

    public MWPacket10UserIngameConnection() { }

    public MWPacket10UserIngameConnection(int userId, UUID userSession, String address) {
        this.userId = userId;
        this.userSession = userSession;
        this.address = address;
    }

    @Override
    public int getId() {
        return 10;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeInt(this.userId);
        buffer.writeUUID(this.userSession);
        buffer.writeString(this.address);
    }

    @Override
    public void read(Buffer buffer) {
        this.userId = buffer.readInt();
        this.userSession = buffer.readUUID();
        this.address = buffer.readString(16);
    }
}
