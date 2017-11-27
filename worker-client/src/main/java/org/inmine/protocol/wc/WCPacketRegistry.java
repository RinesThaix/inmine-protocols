package org.inmine.protocol.wc;

import org.inmine.network.PacketRegistry;
import org.inmine.protocol.wc.packet.CPacket1Login;
import org.inmine.protocol.wc.packet.CPacket2LoginResponse;
import org.inmine.protocol.wc.packet.CPacket3UserInfo;
import org.inmine.protocol.wc.packet.CPacket4ChangeName;
import org.inmine.protocol.wc.packet.CPacket5IngameConnection;
import org.inmine.protocol.wc.packet.CPacket6IngameConnectionResult;
import org.inmine.protocol.wc.packet.CPacket7IngameDisconnect;

/**
 * Created by RINES on 24.11.17.
 */
public class WCPacketRegistry extends PacketRegistry {

    private static final WCPacketRegistry INSTANCE = new WCPacketRegistry();

    @SuppressWarnings("unchecked")
    private WCPacketRegistry() {
        super(1,
            CPacket1Login::new,
            CPacket2LoginResponse::new,
            CPacket3UserInfo::new,
            CPacket4ChangeName::new,
            CPacket5IngameConnection::new,
            CPacket6IngameConnectionResult::new,
            CPacket7IngameDisconnect::new
        );
    }

    public static WCPacketRegistry instance() {
        return INSTANCE;
    }

}
