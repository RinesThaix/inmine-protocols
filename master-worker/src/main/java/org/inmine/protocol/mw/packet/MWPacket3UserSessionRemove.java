package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * @author xtrafrancyz
 */
public class MWPacket3UserSessionRemove extends Packet {
    public int userId;
    public UUID session;

    public MWPacket3UserSessionRemove() { }

    public MWPacket3UserSessionRemove(int userId, UUID session) {
        this.userId = userId;
        this.session = session;
    }

    @Override
    public int getId() {
        return 3;
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
