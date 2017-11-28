package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.callback.CallbackPacket;

import java.util.UUID;

/**
 * @author xtrafrancyz
 */
public class PPacket11VoiceRoomCreated extends CallbackPacket {
    public UUID roomId;

    public PPacket11VoiceRoomCreated() { }

    public PPacket11VoiceRoomCreated(UUID roomId) {
        this.roomId = roomId;
    }

    @Override
    public int getId() {
        return 11;
    }

    @Override
    public void write(Buffer buffer) {
        super.write(buffer);
        buffer.writeUUID(this.roomId);
    }

    @Override
    public void read(Buffer buffer) {
        super.read(buffer);
        this.roomId = buffer.readUUID();
    }
}
