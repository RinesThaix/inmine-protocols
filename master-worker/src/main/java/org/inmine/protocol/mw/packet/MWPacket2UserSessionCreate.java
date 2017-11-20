package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * @author xtrafrancyz
 */
public class MWPacket2UserSessionCreate extends Packet {
    public int userId;
    public UUID session;

    public MWPacket2UserSessionCreate() { }

    public MWPacket2UserSessionCreate(int userId, UUID session) {
        this.userId = userId;
        this.session = session;
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeInt(this.userId);
        buffer.writeLong(this.session.getMostSignificantBits());
        buffer.writeLong(this.session.getLeastSignificantBits());
    }

    @Override
    public void read(Buffer buffer) {
        this.userId = buffer.readInt();
        this.session = new UUID(buffer.readLong(), buffer.readLong());
    }
}
