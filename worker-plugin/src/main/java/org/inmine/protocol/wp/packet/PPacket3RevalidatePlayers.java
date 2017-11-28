package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * @author xtrafrancyz
 */
public class PPacket3RevalidatePlayers extends Packet {
    public int[] userIds;

    public PPacket3RevalidatePlayers() { }

    public PPacket3RevalidatePlayers(int[] userIds) {
        this.userIds = userIds;
    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeIntArray(userIds, buffer::writeInt);
    }

    @Override
    public void read(Buffer buffer) {
        userIds = buffer.readIntArray(500, buffer::readInt);
    }
}
