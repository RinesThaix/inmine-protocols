package org.inmine.protocol.mt.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * @author xtrafrancyz
 */
public class MTPacket5DeleteVoiceRoom extends Packet {
    public UUID roomId;

    public MTPacket5DeleteVoiceRoom() { }

    public MTPacket5DeleteVoiceRoom(UUID roomId) {
        this.roomId = roomId;
    }

    @Override
    public int getId() {
        return 5;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeUUID(roomId);
    }

    @Override
    public void read(Buffer buffer) {
        roomId = buffer.readUUID();
    }
}
