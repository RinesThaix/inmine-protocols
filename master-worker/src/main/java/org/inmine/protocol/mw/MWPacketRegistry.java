package org.inmine.protocol.mw;

import org.inmine.network.PacketRegistry;
import org.inmine.protocol.mw.packet.*;

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
            MWPacket8ProjectSecretKeyChanged::new,
            MWPacket9UserSessionToPluginSession::new,
            MWPacket10UserIngameConnection::new,
            MWPacket11AwaitPlayerOnPlugin::new
        );
    }
}
