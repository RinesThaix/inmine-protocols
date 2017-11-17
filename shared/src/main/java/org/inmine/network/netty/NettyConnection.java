package org.inmine.network.netty;

import io.netty.channel.ChannelHandlerContext;

import org.inmine.network.Connection;

import java.lang.ref.WeakReference;

/**
 * Created by RINES on 17.11.17.
 */
public class NettyConnection implements Connection {
    
    private final ChannelHandlerContext context;
    private final WeakReference<NettyPacketHandler> handler;
    
    public NettyConnection(ChannelHandlerContext context, NettyPacketHandler handler) {
        this.context = context;
        this.handler = new WeakReference<>(handler);
    }
    
    public ChannelHandlerContext getContext() {
        return this.context;
    }
    
    public NettyPacketHandler getHandler() {
        return this.handler.get();
    }
}
