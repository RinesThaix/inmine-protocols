package org.inmine.protocol.wp;

import org.inmine.network.PacketRegistry;
import org.inmine.protocol.wp.packet.PPacket1Connect;
import org.inmine.protocol.wp.packet.PPacket2ConnectionResponse;
import org.inmine.protocol.wp.packet.PPacket3ProjectNameChange;

/**
 * Created by RINES on 21.11.17.
 */
public class WPPacketRegistry extends PacketRegistry {

    @SuppressWarnings("unchecked")
    public WPPacketRegistry() {
        super(1,
            PPacket1Connect::new,
            PPacket2ConnectionResponse::new,
            PPacket3ProjectNameChange::new
        );
    }

}
