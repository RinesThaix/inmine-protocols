package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * Created by RINES on 29.11.17.
 */
public class PPacket14ChatMessage extends Packet {

    public String player;
    public Message message;

    public PPacket14ChatMessage() { }

    public PPacket14ChatMessage(String player, Message message) {
        this.player = player;
        this.message = message;
    }

    @Override
    public int getId() {
        return 14;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeString(this.player);
        buffer.writeEnum(this.message);
    }

    @Override
    public void read(Buffer buffer) {
        this.player = buffer.readString(40);
        this.message = buffer.readEnum(Message.class);
    }

    public enum Message {
        INVALID_SECRET_KEY,
        COULD_NOT_FIND_USER_SESSION_WHILST_VALIDATING
    }

}
