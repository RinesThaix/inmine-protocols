package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.callback.CallbackPacket;

/**
 * @author xtrafrancyz
 */
public class PPacket10CreateVoiceRoom extends CallbackPacket {
    public Type type;

    public PPacket10CreateVoiceRoom() { }

    public PPacket10CreateVoiceRoom(Type type) {
        this.type = type;
    }

    @Override
    public int getId() {
        return 10;
    }

    @Override
    public void write(Buffer buffer) {
        super.write(buffer);
        buffer.writeEnum(this.type);
    }

    @Override
    public void read(Buffer buffer) {
        super.read(buffer);
        this.type = buffer.readEnum(Type.class);
    }

    public enum Type {
        POSITIONAL,
        NORMAL
    }
}
