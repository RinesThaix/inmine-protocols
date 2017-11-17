package org.inmine.network;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by RINES on 17.11.17.
 */
public class PacketRegistry {
    
    private final Map<Integer, Supplier<Packet>> constructors = new HashMap<>();
    
    public PacketRegistry(Supplier<Packet>... constructors) {
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
    
}
