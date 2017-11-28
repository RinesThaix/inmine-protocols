package org.inmine.protocol.wp.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.List;

/**
 * @author xtrafrancyz
 */
public class PPacket3RevalidatePlayers extends Packet {
    public List<String> nicknames;

    public PPacket3RevalidatePlayers() { }

    public PPacket3RevalidatePlayers(List<String> nicknames) {
        this.nicknames = nicknames;
    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeStringList(nicknames);
    }

    @Override
    public void read(Buffer buffer) {
        nicknames = buffer.readStringList(500, 40);
    }
}
