package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * @author xtrafrancyz
 */
public class MWPacket1Connect extends Packet {
    @Override
    public int getId() {
        return 1;
    }
    
    @Override
    public void write(Buffer buffer) {
        
    }
    
    @Override
    public void read(Buffer buffer) {
        
    }
}
