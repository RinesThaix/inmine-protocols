package org.inmine.network.netty.server;

import io.netty.channel.socket.SocketChannel;

import org.inmine.network.NetworkStatisticsImpl;
import org.inmine.network.netty.ChannelInitializer;

/**
 * Created by RINES on 17.11.17.
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    public ServerChannelInitializer(NettyServer server) {
        super(
            (NetworkStatisticsImpl) server.getStatistics(),
            server.getPacketRegistry(),
            () -> new NettyServerPacketHandler(server),
            server.getLogger()
        );
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ch.config().setTcpNoDelay(false);
        super.initChannel(ch);
    }
}
