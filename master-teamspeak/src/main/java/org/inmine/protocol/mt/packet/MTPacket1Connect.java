package org.inmine.protocol.mt.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * @author xtrafrancyz
 */
public class MTPacket1Connect extends Packet {
    public int maxUsers;

    public MTPacket1Connect() { }

    public MTPacket1Connect(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeVarInt(this.maxUsers);
    }

    @Override
    public void read(Buffer buffer) {
        this.maxUsers = buffer.readVarInt();
    }
}
