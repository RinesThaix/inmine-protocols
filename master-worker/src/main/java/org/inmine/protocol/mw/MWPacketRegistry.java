package org.inmine.protocol.mw;

import org.inmine.network.PacketRegistry;
import org.inmine.protocol.mw.packet.MWPacket1Connect;
import org.inmine.protocol.mw.packet.MWPacket2UserConnect;
import org.inmine.protocol.mw.packet.MWPacket3UserDisconnect;

/**
 * @author xtrafrancyz
 */
public class MWPacketRegistry extends PacketRegistry {
    public MWPacketRegistry() {
        super(1,
            MWPacket1Connect::new,
            MWPacket2UserConnect::new,
            MWPacket3UserDisconnect::new
        );
    }
}
