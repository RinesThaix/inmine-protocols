package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * Packet from worker node to the plugin or from the plugin to the worker node
 * Created by RINES on 23.11.17.
 */
public class PPacket7UnregisterPlayer extends Packet {

    public String nickname;

    @Override
    public int getId() {
        return 7;
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
