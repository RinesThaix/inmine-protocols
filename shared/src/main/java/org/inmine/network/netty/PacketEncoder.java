package org.inmine.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.inmine.network.Packet;

/**
 * Created by RINES on 17.11.17.
 */
@ChannelHandler.Sharable
public class PacketEncoder extends MessageToByteEncoder<Packet> {
    
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
        ByteBuf temp = ctx.alloc().buffer();
        NettyBuffer buffer = NettyBufferPool.DEFAULT.wrap(temp);
        try {
            buffer.writeSignedVarInt(packet.getId());
            packet.write(buffer);
            
            buffer.setHandle(out);
            buffer.writeVarInt(temp.readableBytes());
            out.writeBytes(temp);
        } finally {
            buffer.release();
            temp.release();
        }
    }
}
