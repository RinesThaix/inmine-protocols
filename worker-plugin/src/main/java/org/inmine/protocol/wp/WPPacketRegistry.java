package org.inmine.protocol.wp;

import org.inmine.network.PacketRegistry;
import org.inmine.protocol.wp.packet.*;

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
            PPacket8PlayerSecretKey::new,
            PPacket9PositionUpdate::new,
            PPacket10CreateVoiceRoom::new,
            PPacket11VoiceRoomCreated::new,
            PPacket12MovePlayer::new,
            PPacket13DeleteVoiceRoom::new
        );
    }

    public static WPPacketRegistry instance() {
        return INSTANCE;
    }

}
