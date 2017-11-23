package org.inmine.protocol.wp;

import org.inmine.network.PacketRegistry;
import org.inmine.protocol.wp.packet.PPacket1Connect;
import org.inmine.protocol.wp.packet.PPacket2ConnectionResponse;
import org.inmine.protocol.wp.packet.PPacket3ProjectNameChange;
import org.inmine.protocol.wp.packet.PPacket4AwaitPlayer;
import org.inmine.protocol.wp.packet.PPacket5PlayerAwaitingResponse;
import org.inmine.protocol.wp.packet.PPacket6RegisterPlayer;
import org.inmine.protocol.wp.packet.PPacket7UnregisterPlayer;

/**
 * Created by RINES on 21.11.17.
 */
public class WPPacketRegistry extends PacketRegistry {

    @SuppressWarnings("unchecked")
    public WPPacketRegistry() {
        super(1,
            PPacket1Connect::new,
            PPacket2ConnectionResponse::new,
            PPacket3ProjectNameChange::new,
            PPacket4AwaitPlayer::new,
            PPacket5PlayerAwaitingResponse::new,
            PPacket6RegisterPlayer::new,
            PPacket7UnregisterPlayer::new
        );
    }

}
