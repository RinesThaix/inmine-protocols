package org.inmine.protocol.wc.packet;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;

/**
 * Created by RINES on 18.11.17.
 */
public class CPacket1Login extends Packet {

    public String email;
    public String password;

    public CPacket1Login() { }

    public CPacket1Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeString(this.email);
        buffer.writeString(this.password);
    }

    @Override
    public void read(Buffer buffer) {
        this.email = buffer.readString(255);
        this.password = buffer.readString(255);
    }
}
