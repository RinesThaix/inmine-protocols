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
        int initialReaderIndex = buf.readerIndex();
        NettyBuffer buffer = new NettyBuffer(buf);
        int length = buffer.readVarInt();
        if (length >= 1_000_000)
            throw new DecoderException("Maximum allowed packet length is " + 1_000_000 + ", received " + length);
        
        if (buf.readableBytes() < length) {
            buf.readerIndex(initialReaderIndex);
            return;
        }
        
        int id = buffer.readVarInt();
        Packet packet = this.packetRegistry.constructPacket(id);
        if (packet == null) {
            buf.skipBytes(length);
            throw new DecoderException("Unknown packet ID " + id + ", size=" + length);
        }
        
        try {
            packet.read(buffer);
            // TODO проверить что считаны не все байты и скипнуть остаток
        } catch (Exception ex) {
            throw new DecoderException("Decoding packet ID " + id + ", size=" + length, ex);
        }
        out.add(packet);
    }
}
