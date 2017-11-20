package org.inmine.protocol.mw;

import org.inmine.network.PacketRegistry;
import org.inmine.protocol.mw.packet.MWPacket1Connect;
import org.inmine.protocol.mw.packet.MWPacket2UserSessionCreate;
import org.inmine.protocol.mw.packet.MWPacket3UserSessionRemove;
import org.inmine.protocol.mw.packet.MWPacket4UserNameChanged;

/**
 * @author xtrafrancyz
 */
public class MWPacketRegistry extends PacketRegistry {
    @SuppressWarnings("unchecked")
    public MWPacketRegistry() {
        super(1,
            MWPacket1Connect::new,
            MWPacket2UserSessionCreate::new,
            MWPacket3UserSessionRemove::new,
            MWPacket4UserNameChanged::new
        );
    }
}
