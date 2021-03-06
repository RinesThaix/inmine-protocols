package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.callback.CallbackPacket;

/**
 * @author xtrafrancyz
 */
public class PPacket10CreateVoiceRoom extends CallbackPacket {
    public Type type;
    public int radius;

    public PPacket10CreateVoiceRoom() { }

    public PPacket10CreateVoiceRoom(Type type) {
        this.type = type;
    }

    public PPacket10CreateVoiceRoom(Type type, int radius) {
        this.type = type;
        this.radius = radius;
    }

    @Override
    public int getId() {
        return 10;
    }

    @Override
    public void write(Buffer buffer) {
        super.write(buffer);
        buffer.writeEnum(this.type);
        if (type == Type.POSITIONAL)
            buffer.writeVarInt(radius);
    }

    @Override
    public void read(Buffer buffer) {
        super.read(buffer);
        this.type = buffer.readEnum(Type.class);
        if (type == Type.POSITIONAL)
            radius = buffer.readVarInt();
    }

    public enum Type {
        POSITIONAL,
        NORMAL
    }
}
