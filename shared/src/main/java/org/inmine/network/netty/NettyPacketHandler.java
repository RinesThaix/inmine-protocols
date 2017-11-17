package org.inmine.network.netty;

import io.netty.channel.ChannelHandlerContext;

import org.inmine.network.PacketHandler;

/**
 * Created by RINES on 17.11.17.
 */
public abstract class NettyPacketHandler extends PacketHandler {
    
    public abstract void onConnect(ChannelHandlerContext ctx);
    
    public abstract void onDisconnect(ChannelHandlerContext ctx);
    
    @Override
    public NettyConnection getConnection() {
        return (NettyConnection) super.getConnection();
    }
    
}
