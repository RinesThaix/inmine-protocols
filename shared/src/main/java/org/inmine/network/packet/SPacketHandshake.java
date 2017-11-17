package org.inmine.network.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * @author xtrafrancyz
 */
public class SPacketHandshake extends Packet {
    public int version;
    
    public SPacketHandshake() {}
    
    public SPacketHandshake(int version) {
        this.version = version;
    }
    
    @Override
    public int getId() {
        return -1;
    }
    
    @Override
    public void write(Buffer buffer) {
        buffer.writeVarInt(version);
    }
    
    @Override
    public void read(Buffer buffer) {
        version = buffer.readVarInt();
    }
}
