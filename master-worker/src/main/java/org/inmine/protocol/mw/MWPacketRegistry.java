package org.inmine.protocol.mw;

import org.inmine.network.PacketRegistry;
import org.inmine.protocol.mw.packet.MWPacket1Connect;
import org.inmine.protocol.mw.packet.MWPacket2UserSessionCreate;
import org.inmine.protocol.mw.packet.MWPacket3UserSessionRemove;
import org.inmine.protocol.mw.packet.MWPacket4UserNameChanged;
import org.inmine.protocol.mw.packet.MWPacket5PluginSessionCreate;
import org.inmine.protocol.mw.packet.MWPacket6PluginSessionRemove;
import org.inmine.protocol.mw.packet.MWPacket7ProjectNameChanged;
import org.inmine.protocol.mw.packet.MWPacket8ProjectSecretKeyChanged;

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
            MWPacket4UserNameChanged::new,
            MWPacket5PluginSessionCreate::new,
            MWPacket6PluginSessionRemove::new,
            MWPacket7ProjectNameChanged::new,
            MWPacket8ProjectSecretKeyChanged::new
        );
    }
}
