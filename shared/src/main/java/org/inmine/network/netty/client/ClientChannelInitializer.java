package org.inmine.network.netty.client;

import io.netty.channel.Channel;

import org.inmine.network.NetworkStatisticsImpl;
import org.inmine.network.netty.ChannelInitializer;

/**
 * Created by RINES on 17.11.17.
 */
public class ClientChannelInitializer extends ChannelInitializer<Channel> {

    public ClientChannelInitializer(NettyClient client) {
        super(
            (NetworkStatisticsImpl) client.getStatistics(),
            client.getPacketRegistry(),
            () -> new NettyClientPacketHandler(client),
            client.getLogger()
        );
    }

}
