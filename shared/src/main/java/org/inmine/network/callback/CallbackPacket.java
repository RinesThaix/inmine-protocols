package org.inmine.network.callback;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * @author xtrafrancyz
 */
public abstract class CallbackPacket extends Packet {

    public int callbackId;
    
    @Override
    public void write(Buffer buffer) {
        buffer.writeVarInt(this.callbackId);
    }
    
    @Override
    public void read(Buffer buffer) {
        this.callbackId = buffer.readVarInt();
    }

}
