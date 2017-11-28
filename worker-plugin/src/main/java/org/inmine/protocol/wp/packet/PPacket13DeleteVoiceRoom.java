package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * @author xtrafrancyz
 */
public class PPacket13DeleteVoiceRoom extends Packet {
    public UUID roomId;

    public PPacket13DeleteVoiceRoom() { }

    public PPacket13DeleteVoiceRoom(UUID roomId) {
        this.roomId = roomId;
    }

    @Override
    public int getId() {
        return 13;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeUUID(roomId);
    }

    @Override
    public void read(Buffer buffer) {
        this.roomId = buffer.readUUID();
    }
}
