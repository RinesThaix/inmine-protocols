package org.inmine.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.inmine.network.NetworkStatisticsImpl;
import org.inmine.network.Packet;

/**
 * Created by RINES on 17.11.17.
 */
@ChannelHandler.Sharable
public class PacketEncoder extends MessageToByteEncoder<Packet> {

    private NetworkStatisticsImpl statistics;

    public PacketEncoder(NetworkStatisticsImpl statistics) {
        this.statistics = statistics;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
        ByteBuf temp = ctx.alloc().buffer();
        NettyBuffer buffer = NettyBuffer.newInstance(temp);
        try {
            buffer.writeSignedVarInt(packet.getId());
            packet.write(buffer);

            int start = out.writerIndex();

            buffer.setHandle(out);
            buffer.writeVarInt(temp.readableBytes());
            out.writeBytes(temp);

            statistics.sentPackets().addAndGet(1);
            statistics.sentBytes().addAndGet(out.writerIndex() - start);
        } finally {
            buffer.release();
            temp.release();
        }
    }
}
