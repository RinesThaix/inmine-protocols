package org.inmine.protocol.mw.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.inmine.network.Buffer;
import org.inmine.network.Packet;
import org.inmine.network.PacketRegistry;
import org.inmine.network.netty.NettyBuffer;
import org.inmine.network.netty.NettyBufferPool;

/**
 * @author xtrafrancyz
 */
abstract class PacketContainer extends Packet {
    private Packet packet;
    private byte[] serialized;

    protected PacketContainer() { }

    protected PacketContainer(Packet packet) {
        this.packet = packet;
    }

    @Override
    public void write(Buffer buffer) {
        if (serialized != null) {
            buffer.writeVarInt(serialized.length);
            buffer.writeBytes(serialized);
        } else {
            // Packet serialization
            Buffer temp = buffer.newBuffer(256);
            try {
                temp.writeVarInt(packet.getId());
                packet.write(temp);
                ByteBuf tempNetty = ((NettyBuffer) temp).getHandle();

                // Write packet
                buffer.writeVarInt(tempNetty.readableBytes());
                ((NettyBuffer) buffer).getHandle().writeBytes(tempNetty);
            } finally {
                temp.release();
            }
        }
    }

    @Override
    public void read(Buffer buffer) {
        serialized = buffer.readByteArray(1 << 16); // 65536
    }

    public Packet getPacket(PacketRegistry registry) {
        if (packet == null) {
            NettyBuffer buffer = NettyBufferPool.DEFAULT.wrap(Unpooled.wrappedBuffer(serialized));
            try {
                int id = buffer.readVarInt();
                Packet packet = registry.constructPacket(id);
                if (packet == null)
                    throw new IllegalStateException("Unknown packet ID " + id + " for " + registry);
                packet.read(buffer);
                this.packet = packet;
            } finally {
                buffer.release();
            }
        }
        return packet;
    }
}
