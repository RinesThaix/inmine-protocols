package org.inmine.network.netty.client;

import io.netty.channel.ChannelHandlerContext;

import org.inmine.network.Packet;
import org.inmine.network.netty.NettyPacketHandler;

/**
 * Created by RINES on 17.11.17.
 */
public class NettyClientPacketHandler extends NettyPacketHandler {
    
    private final NettyClient client;
    
    public NettyClientPacketHandler(NettyClient client) {
        this.client = client;
    }
    
    @Override
    public void handle(Packet packet) {
        this.client.onPacketPreReceived(packet);
        super.handle(packet);
    }
    
    @Override
    public void onConnect(ChannelHandlerContext ctx) {
        setConnection(this.client.createConnection(ctx, this));
    }
    
    @Override
    public void onDisconnect(ChannelHandlerContext ctx) {
        this.client.deleteConnection(ctx);
    }
    
}
