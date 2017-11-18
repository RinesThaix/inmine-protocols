package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * @author xtrafrancyz
 */
public class MWPacket2UserConnect extends Packet {
    public int userId;
    
    public MWPacket2UserConnect() { }
    
    public MWPacket2UserConnect(int userId) {
        this.userId = userId;
    }
    
    @Override
    public int getId() {
        return 2;
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
