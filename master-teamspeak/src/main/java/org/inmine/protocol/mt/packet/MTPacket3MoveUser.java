package org.inmine.protocol.mt.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * @author xtrafrancyz
 */
public class MTPacket3MoveUser extends Packet {
    public int userId;
    public UUID roomId;

    public MTPacket3MoveUser() { }

    public MTPacket3MoveUser(int userId, UUID roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeInt(userId);
        buffer.writeUUIDNullable(roomId);
    }

    @Override
    public void read(Buffer buffer) {
        userId = buffer.readInt();
        roomId = buffer.readUUIDNullable();
    }
}
