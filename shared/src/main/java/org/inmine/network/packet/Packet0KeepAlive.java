package org.inmine.network.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * Created by RINES on 17.11.17.
 */
public class Packet0KeepAlive extends Packet {
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void write(Buffer buffer) {

    }

    @Override
    public void read(Buffer buffer) {

    }
}
