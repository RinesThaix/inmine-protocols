package org.inmine.protocol.mt;

import org.inmine.network.PacketRegistry;
import org.inmine.protocol.mt.packet.MTPacket1Connect;
import org.inmine.protocol.mt.packet.MTPacket2CreateVoiceRoom;
import org.inmine.protocol.mt.packet.MTPacket3MoveUser;
import org.inmine.protocol.mt.packet.MTPacket4PositionUpdate;
import org.inmine.protocol.mt.packet.MTPacket5DeleteVoiceRoom;

/**
 * @author xtrafrancyz
 */
public class MTPacketRegistry extends PacketRegistry {
    @SuppressWarnings("unchecked")
    public MTPacketRegistry() {
        super(1,
            MTPacket1Connect::new,
            MTPacket2CreateVoiceRoom::new,
            MTPacket3MoveUser::new,
            MTPacket4PositionUpdate::new,
            MTPacket5DeleteVoiceRoom::new
        );
    }
}
