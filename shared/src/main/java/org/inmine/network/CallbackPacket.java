package org.inmine.network;

/**
 * @author xtrafrancyz
 */
public abstract class CallbackPacket extends Packet {
    public int callbackId;
    
    @Override
    public void write(Buffer buffer) {
        buffer.writeVarInt(callbackId);
    }
    
    @Override
    public void read(Buffer buffer) {
        callbackId = buffer.readVarInt();
    }
}
