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
        ByteBuf buf = ctx.alloc().buffer();
        NettyBuffer buffer = new NettyBuffer(buf);
        try {
            buffer.writeShort((short) packet.getId());
            packet.write(buffer);
            NettyBuffer outbuffer = new NettyBuffer(out);
            outbuffer.writeVarInt(buf.readableBytes());
            outbuffer.getByteBuf().writeBytes(buf);
        } finally {
            buf.release();
        }
    }
}
