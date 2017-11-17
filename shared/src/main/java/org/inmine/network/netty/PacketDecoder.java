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
    
    private final NettyBufferPool bufferPool;
    private final PacketRegistry packetRegistry;
    
    public PacketDecoder(NettyBufferPool bufferPool, PacketRegistry packetRegistry) {
        this.bufferPool = bufferPool;
        this.packetRegistry = packetRegistry;
    }
    
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        int readerIndex = buf.readerIndex();
        NettyBuffer buffer = bufferPool.wrap(buf);
        try {
            int length = buffer.readVarInt();
            if (length >= 1_000_000)
                throw new DecoderException("Maximum allowed packet length is " + 1_000_000 + ", received " + length);
            
            if (buf.readableBytes() < length) {
                buf.readerIndex(readerIndex);
                return;
            }
            
            readerIndex = buf.readerIndex();
            
            int id = buffer.readVarInt();
            Packet packet = this.packetRegistry.constructPacket(id);
            if (packet == null) {
                buf.skipBytes(length);
                throw new DecoderException("Unknown packet ID " + id + ", size=" + length);
            }
            
            try {
                packet.read(buffer);
                
                // Если не считаны все байты
                if (buf.readerIndex() - readerIndex != length) {
                    int toSkip = length - (buf.readerIndex() - readerIndex);
                    buf.skipBytes(toSkip);
                    ctx.channel().attr(HandlerBoss.BOSS_KEY).get().getLogger().warning(
                        "After reading packet ID " + id + ", there are " + toSkip + " bytes left (length " + length + "). Packet ignored."
                    );
                    return;
                }
            } catch (Exception ex) {
                throw new DecoderException("Decoding packet ID " + id + ", size=" + length, ex);
            }
            out.add(packet);
        } finally {
            buffer.release();
        }
    }
}
