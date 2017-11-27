package org.inmine.protocol.mw;

import org.inmine.network.PacketRegistry;
import org.inmine.protocol.mw.packet.*;

/**
 * @author xtrafrancyz
 */
public class MWPacketRegistry extends PacketRegistry {

    private static final MWPacketRegistry INSTANCE = new MWPacketRegistry();

    @SuppressWarnings("unchecked")
    private MWPacketRegistry() {
        super(1,
            MWPacket1Connect::new,
            MWPacket2UserSessionCreate::new,
            MWPacket3UserSessionRemove::new,
            MWPacket4PluginSessionCreate::new,
            MWPacket5PluginSessionRemove::new,
            MWPacket6ProjectSecretKeyChanged::new,
            MWPacket7UserIngameConnection::new,
            MWPacket8AwaitPlayerOnPlugin::new,
            MWPacket9UserIngameConnectionResponse::new,
            MWPacket10Proxy::new,
            MWPacket11ProxyBroadcast::new,
            MWPacket12ProxyToMaster::new
        );
    }

    public static MWPacketRegistry instance() {
        return INSTANCE;
    }

}
