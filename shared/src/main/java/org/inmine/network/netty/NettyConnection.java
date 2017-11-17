package org.inmine.network.netty;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import org.inmine.network.Connection;
import org.inmine.network.Packet;
import org.inmine.network.callback.CallbackHandler;
import org.inmine.network.callback.CallbackPacket;

import java.util.function.Consumer;

/**
 * Created by RINES on 17.11.17.
 */
public class NettyConnection implements Connection {
    
    private final CallbackHandler parent;
    private final ChannelHandlerContext context;
    private final NettyPacketHandler handler;
    
    public NettyConnection(CallbackHandler parent, ChannelHandlerContext context, NettyPacketHandler handler) {
        this.parent = parent;
        this.context = context;
        this.handler = handler;
    }
    
    public ChannelHandlerContext getContext() {
        return this.context;
    }
    
    @Override
    public NettyPacketHandler getHandler() {
        return this.handler;
    }
    
    @Override
    public void sendPacket(Packet packet) {
        handler.packetSent();
        context.writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
    
    @Override
    public <T extends CallbackPacket> void sendPacket(T packet, Consumer<CallbackPacket> callback, long timeout, Runnable onTimeout) {
        parent.registerCallback(packet, callback, timeout, onTimeout);
        sendPacket(packet);
    }
    
    @Override
    public void disconnect() {
        context.channel().close();
    }
}
