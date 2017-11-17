package org.inmine.network;

import org.inmine.network.packet.SPacketDisconnect;
import org.inmine.network.packet.SPacketHandshake;
import org.inmine.network.packet.SPacketKeepAlive;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by RINES on 17.11.17.
 */
public class PacketRegistry {
    private static final int SHARED_PROTOCOL_VERSION = 1;
    
    private final int version;
    private final Map<Integer, Supplier<Packet>> constructors = new HashMap<>();
    
    public PacketRegistry(int version, Supplier<Packet>... constructors) {
        this.version = version;
        this.constructors.put(0, SPacketKeepAlive::new);
        this.constructors.put(-1, SPacketHandshake::new);
        this.constructors.put(-2, SPacketDisconnect::new);
        for (Supplier<Packet> constructor : constructors) {
            Packet packet = constructor.get();
            this.constructors.put(packet.getId(), constructor);
        }
    }
    
    public Packet constructPacket(int id) {
        Supplier<Packet> constructor = this.constructors.get(id);
        if (constructor == null)
            return null;
        return constructor.get();
    }
    
    public int getVersion() {
        return version + 1000 * SHARED_PROTOCOL_VERSION;
    }
}
