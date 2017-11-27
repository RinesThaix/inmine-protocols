package org.inmine.protocol.wp;

import org.inmine.network.PacketRegistry;
import org.inmine.protocol.wp.packet.PPacket1Connect;
import org.inmine.protocol.wp.packet.PPacket2ConnectionResponse;
import org.inmine.protocol.wp.packet.PPacket4AwaitPlayer;
import org.inmine.protocol.wp.packet.PPacket5PlayerAwaitingResponse;
import org.inmine.protocol.wp.packet.PPacket6RegisterPlayer;
import org.inmine.protocol.wp.packet.PPacket7UnregisterPlayer;
import org.inmine.protocol.wp.packet.PPacket8PlayerSecretKey;

/**
 * Created by RINES on 21.11.17.
 */
public class WPPacketRegistry extends PacketRegistry {

    private static final WPPacketRegistry INSTANCE = new WPPacketRegistry();

    @SuppressWarnings("unchecked")
    public WPPacketRegistry() {
        super(1,
            PPacket1Connect::new,
            PPacket2ConnectionResponse::new,
            PPacket4AwaitPlayer::new,
            PPacket5PlayerAwaitingResponse::new,
            PPacket6RegisterPlayer::new,
            PPacket7UnregisterPlayer::new,
            PPacket8PlayerSecretKey::new
        );
    }

    public static WPPacketRegistry instance() {
        return INSTANCE;
    }

}
