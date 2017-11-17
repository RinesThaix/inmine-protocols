package org.inmine.network.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * @author xtrafrancyz
 */
public class SPacketDisconnect extends Packet {
    public String message;
    
    public SPacketDisconnect() {}
    
    public SPacketDisconnect(String message) {
        this.message = message;
    }
    
    @Override
    public int getId() {
        return -2;
    }
    
    @Override
    public void write(Buffer buffer) {
        buffer.writeStringNullable(message);
    }
    
    @Override
    public void read(Buffer buffer) {
        buffer.readStringNullable(255);
    }
}
