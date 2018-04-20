package org.inmine.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;

import org.inmine.network.NetworkStatisticsImpl;
import org.inmine.network.Packet;
import org.inmine.network.PacketRegistry;

import java.util.List;

/**
 * Created by RINES on 17.11.17.
 */
public class PacketDecoder extends ByteToMessageDecoder {

    private final NetworkStatisticsImpl statistics;
    private final PacketRegistry packetRegistry;

    public PacketDecoder(NetworkStatisticsImpl statistics, PacketRegistry packetRegistry) {
        this.statistics = statistics;
        this.packetRegistry = packetRegistry;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        int readerIndex = buf.readerIndex();

        int length = 0;
        int lengthOfLength = 0;

        { // Считывания варинта, пока есть данные. Если данных не хватает - ждем
            byte in;
            while (true) {
                // Костыль для проверки что у нас еще есть что читать
                if (buf.readableBytes() == 0) {
                    buf.readerIndex(readerIndex);
                    return;
                }

                in = buf.readByte();

                length |= (in & 0x7F) << (lengthOfLength++ * 7);

                if (lengthOfLength > 5)
                    throw new DecoderException("Wrong packet length");

                if ((in & 0x80) != 0x80)
                    break;
            }
        }

        if (length < 0)
            throw new DecoderException("Packet length must be >= zero , received " + length);

        if (length > 1_000_000)
            throw new DecoderException("Maximum allowed packet length is " + 1_000_000 + ", received " + length);

        // Пакет пришел не полностью
        if (buf.readableBytes() < length) {
            buf.readerIndex(readerIndex);
            return;
        }

        NettyBuffer buffer = NettyBuffer.newInstance(buf);
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
                    int diff = length - (buf.readerIndex() - readerIndex);
                    ctx.channel().attr(HandlerBoss.BOSS_KEY).get().getLogger().warning(
                        "After reading packet " + packet.getClass().getSimpleName() + ", there are " +
                            (diff > 0 ? diff + " bytes left" : -diff + " extra bytes read") +
                            " (length " + length + "). Packet ignored."
                    );
                    buf.readerIndex(readerIndex + length);
                    return;
                }
            } catch (Exception ex) {
                throw new DecoderException("Decoding packet " + packet.getClass().getSimpleName() + ", size=" + length, ex);
            }

            statistics.receivedPackets().addAndGet(1);
            statistics.receivedBytes().addAndGet(length + lengthOfLength);

            out.add(packet);
        } finally {
            buffer.release();
        }
    }
}
