package org.inmine.protocol.mw.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

import java.util.UUID;

/**
 * Created by RINES on 23.11.17.
 */
public class MWPacket7UserIngameConnection extends Packet {

    public UUID userSession;
    public String userAddress;
    public String serverAddress;

    public MWPacket7UserIngameConnection() { }

    public MWPacket7UserIngameConnection(UUID userSession, String userAddress, String serverAddress) {
        this.userSession = userSession;
        this.userAddress = userAddress;
        this.serverAddress = serverAddress;
    }

    @Override
    public int getId() {
        return 7;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeUUID(this.userSession);
        buffer.writeString(this.userAddress);
        buffer.writeString(this.serverAddress);
    }

    @Override
    public void read(Buffer buffer) {
        this.userSession = buffer.readUUID();
        this.userAddress = buffer.readString(ADDRESS_SIZE);
        this.serverAddress = buffer.readString(ADDRESS_SIZE);
    }
}
