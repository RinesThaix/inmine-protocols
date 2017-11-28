package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * @author xtrafrancyz
 */
public class PPacket12MovePlayer extends Packet {
    public int userId;
    public UUID roomId;

    public PPacket12MovePlayer() { }

    public PPacket12MovePlayer(int userId, UUID roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }

    @Override
    public int getId() {
        return 12;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeUUIDNullable(this.roomId);
        buffer.writeInt(this.userId);
    }

    @Override
    public void read(Buffer buffer) {
        this.roomId = buffer.readUUIDNullable();
        this.userId = buffer.readInt();
    }
}
