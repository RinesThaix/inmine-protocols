package org.inmine.network.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;

import org.inmine.network.Packet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by RINES on 17.11.17.
 */
public class HandlerBoss extends ChannelInboundHandlerAdapter {
    
    public static final AttributeKey<HandlerBoss> BOSS_KEY = AttributeKey.newInstance("HandlerBoss");
    
    private NettyPacketHandler handler;
    private final Logger logger;
    
    public HandlerBoss(NettyPacketHandler initialHandler, Logger logger) {
        this.handler = initialHandler;
        this.logger = logger;
    }
    
    public Logger getLogger() {
        return logger;
    }
    
    public NettyPacketHandler getHandler() {
        return this.handler;
    }
    
    public void setHandler(NettyPacketHandler handler) {
        this.handler = handler;
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.handler.onConnect(ctx);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.handler.onDisconnect(ctx);
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            this.handler.handle((Packet) msg);
        } catch (Exception ex) {
            throw new Exception("Cannot handle " + msg.getClass().getSimpleName() + " for " + ctx.channel().remoteAddress());
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ctx.channel().isActive()) {
            if (cause instanceof TimeoutException) {
                this.logger.log(Level.INFO, ctx.channel().remoteAddress() + " - read timed out");
            } else if (cause instanceof IOException) {
                this.logger.log(Level.INFO, ctx.channel().remoteAddress() + " - IOException: " + cause.getMessage());
            } else if (cause instanceof DecoderException) {
                this.logger.log(Level.INFO, ctx.channel().remoteAddress() + " - Decoder exception: ", cause);
            } else {
                this.logger.log(Level.WARNING, ctx.channel().remoteAddress() + " - Encountered exception", cause);
            }
            ctx.close();
        }
    }
}
