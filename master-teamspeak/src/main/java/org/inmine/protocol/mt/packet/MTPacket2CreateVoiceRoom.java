package org.inmine.protocol.mt.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * @author xtrafrancyz
 */
public class MTPacket2CreateVoiceRoom extends Packet {
    public UUID roomId;
    public Type type;
    public int radius;

    public MTPacket2CreateVoiceRoom() { }

    public MTPacket2CreateVoiceRoom(UUID roomId, Type type) {
        this.roomId = roomId;
        this.type = type;
    }

    public MTPacket2CreateVoiceRoom(UUID roomId, Type type, int radius) {
        this.roomId = roomId;
        this.type = type;
        this.radius = radius;
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeUUID(roomId);
        buffer.writeEnum(type);
        if (type == Type.POSITIONAL)
            buffer.writeVarInt(radius);
    }

    @Override
    public void read(Buffer buffer) {
        roomId = buffer.readUUID();
        type = buffer.readEnum(Type.class);
        if (type == Type.POSITIONAL)
            radius = buffer.readVarInt();
    }

    public enum Type {
        POSITIONAL,
        NORMAL
    }
}
