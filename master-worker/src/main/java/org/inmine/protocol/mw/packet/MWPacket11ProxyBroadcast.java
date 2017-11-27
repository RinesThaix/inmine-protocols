package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * @author xtrafrancyz
 */
public class MWPacket11ProxyBroadcast extends PacketContainer {
    public int destId;
    public Destination destination;

    public MWPacket11ProxyBroadcast() { }

    /**
     * Для назначений WORKERS_OF_USER и WORKERS_OF_PROJECT можно пересылать только пакеты протокола Master-Worker<br/>
     * Для назначения ALL_USER_SESSIONS только протокол Worker-Client
     * Для назначения ALL_PROJECT_SESSIONS только протокол Worker-Plugin
     *
     * @param destId      для бродкаста по проекту - projectId, для бродкаста по юзеру - userId
     * @param destination назначение пакета
     * @param packet      пересылаемый пакет
     */
    public MWPacket11ProxyBroadcast(int destId, Destination destination, Packet packet) {
        super(packet);
        this.destId = destId;
        this.destination = destination;
    }

    @Override
    public int getId() {
        return 11;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeInt(destId);
        buffer.writeEnum(destination);
        super.write(buffer);
    }

    @Override
    public void read(Buffer buffer) {
        destId = buffer.readInt();
        destination = buffer.readEnum(Destination.class);
        super.read(buffer);
    }

    public enum Destination {
        WORKERS_OF_USER,
        ALL_USER_SESSIONS,
        WORKERS_OF_PROJECT,
        ALL_PROJECT_SESSIONS
    }
}
