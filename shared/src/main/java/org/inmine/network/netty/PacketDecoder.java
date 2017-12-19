package org.inmine.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;

import org.inmine.network.Packet;
import org.inmine.network.PacketRegistry;

import java.util.List;

/**
 * Created by RINES on 17.11.17.
 */
public class PacketDecoder extends ByteToMessageDecoder {

    private final PacketRegistry packetRegistry;

    public PacketDecoder(PacketRegistry packetRegistry) {
        this.packetRegistry = packetRegistry;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        int readerIndex = buf.readerIndex();

        int length = safeReadUnsignedVarInt(buf);

        // Не хватает байт для чтения пакета
        if (length == -1 || buf.readableBytes() < length) {
            buf.readerIndex(readerIndex);
            return;
        }

        if (length >= 1_000_000)
            throw new DecoderException("Maximum allowed packet length is " + 1_000_000 + ", received " + length);

        NettyBuffer buffer = NettyBufferPool.DEFAULT.wrap(buf);
        try {
            readerIndex = buf.readerIndex();

            int id = buffer.readSignedVarInt();
            Packet packet = this.packetRegistry.constructPacket(id);
            if (packet == null) {
                buf.skipBytes(length);
                throw new DecoderException("Unknown packet ID " + id + ", size=" + length);
            }

            try {
                packet.read(buffer);

                // Если не считаны все байты
                if (buf.readerIndex() - readerIndex != length) {
                    buf.readerIndex(readerIndex + length);
                    int diff = length - (buf.readerIndex() - readerIndex);
                    ctx.channel().attr(HandlerBoss.BOSS_KEY).get().getLogger().warning(
                        "After reading packet " + packet.getClass().getSimpleName() + ", there are " +
                            (diff > 0 ? diff + " bytes left" : -diff + " extra bytes read") +
                            " (length " + length + "). Packet ignored."
                    );
                    return;
                }
            } catch (Exception ex) {
                throw new DecoderException("Decoding packet " + packet.getClass().getSimpleName() + ", size=" + length, ex);
            }
            out.add(packet);
        } finally {
            buffer.release();
        }
    }

    private static int safeReadUnsignedVarInt(ByteBuf buffer) {
        int out = 0;
        int bytes = 0;
        byte in;
        while (true) {
            if (buffer.readableBytes() == 0)
                return -1;
            in = buffer.readByte();

            out |= (in & 0x7F) << (bytes++ * 7);

            if (bytes > 5)
                throw new DecoderException("VarInt too big");

            if ((in & 0x80) != 0x80)
                break;
        }

        return out;
    }
}
