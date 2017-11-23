package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * Packet from worker node to the plugin
 * Created by RINES on 23.11.17.
 */
public class PPacket6RegisterPlayer extends Packet {

    public String nickname;

    public PPacket6RegisterPlayer() { }

    public PPacket6RegisterPlayer(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public int getId() {
        return 6;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeString(this.nickname);
    }

    @Override
    public void read(Buffer buffer) {
        this.nickname = buffer.readString(16);
    }

}
