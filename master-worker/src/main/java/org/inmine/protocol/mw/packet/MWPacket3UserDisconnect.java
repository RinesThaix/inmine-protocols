package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * @author xtrafrancyz
 */
public class MWPacket3UserDisconnect extends Packet {
    public int userId;
    
    public MWPacket3UserDisconnect() { }
    
    public MWPacket3UserDisconnect(int userId) {
        this.userId = userId;
    }
    
    @Override
    public int getId() {
        return 3;
    }
    
    @Override
    public void write(Buffer buffer) {
        buffer.writeInt(userId);
    }
    
    @Override
    public void read(Buffer buffer) {
        userId = buffer.readInt();
    }
}
