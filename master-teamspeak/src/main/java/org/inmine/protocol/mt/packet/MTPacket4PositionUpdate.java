package org.inmine.protocol.mt.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * @author xtrafrancyz
 */
public class MTPacket4PositionUpdate extends Packet {
    public UUID roomId;
    public int[] data;

    public MTPacket4PositionUpdate() { }

    public MTPacket4PositionUpdate(UUID roomId, int[] data) {
        this.roomId = roomId;
        this.data = data;
    }

    @Override
    public int getId() {
        return 9;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeUUID(roomId);
        buffer.writeVarInt(data.length / 4);
        for (int i = 0; i < data.length; i += 4) {
            // userId
            buffer.writeInt(data[i]);

            // position
            buffer.writeVarInt(data[i + 1]);
            buffer.writeVarInt(data[i + 2]);
            buffer.writeVarInt(data[i + 3]);
        }
    }

    @Override
    public void read(Buffer buffer) {
        this.roomId = buffer.readUUID();
        int len = buffer.readVarInt();
        if (len > 500)
            throw new IllegalStateException("Trying to read " + len + " position updates when the max is 500");
        this.data = new int[len * 4];
        for (int i = 0; i < data.length; i += 4) {
            // userId
            this.data[i] = buffer.readInt();

            // position
            this.data[i + 1] = buffer.readVarInt();
            this.data[i + 2] = buffer.readVarInt();
            this.data[i + 3] = buffer.readVarInt();
        }
    }
}
