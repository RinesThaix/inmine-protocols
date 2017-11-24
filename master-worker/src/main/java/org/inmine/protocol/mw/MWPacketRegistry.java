package org.inmine.protocol.mw;

import org.inmine.network.PacketRegistry;
import org.inmine.protocol.mw.packet.*;
import org.inmine.protocol.wc.WCPacketRegistry;
import org.inmine.protocol.wp.WPPacketRegistry;

/**
 * @author xtrafrancyz
 */
public class MWPacketRegistry extends PacketRegistry {

    private static MWPacketRegistry instance;

    private final WPPacketRegistry pluginPacketRegistry = new WPPacketRegistry();
    private final WCPacketRegistry clientPacketRegistry = new WCPacketRegistry();

    @SuppressWarnings("unchecked")
    public MWPacketRegistry() {
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
            MWPacket10ProxyPluginPacket::new,
            MWPacket11ProxyClientPacket::new
        );
        instance = this;
    }

    public WPPacketRegistry getPluginPacketRegistry() {
        return this.pluginPacketRegistry;
    }

    public WCPacketRegistry getClientPacketRegistry() {
        return this.clientPacketRegistry;
    }

    public static MWPacketRegistry instance() {
        return instance();
    }

}
