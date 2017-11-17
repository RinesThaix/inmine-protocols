package org.inmine.network.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.util.concurrent.ScheduledFuture;

import org.inmine.network.Packet;
import org.inmine.network.PacketRegistry;
import org.inmine.network.callback.AbstractNetworkClient;
import org.inmine.network.netty.NettyConnection;
import org.inmine.network.netty.NettyUtil;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by RINES on 17.11.17.
 */
public abstract class NettyClient extends AbstractNetworkClient {
    
    private final PacketRegistry packetRegistry;
    private final Logger logger;
    
    private ScheduledFuture<?> callbackTickFuture = null;
    private NettyConnection connection;

    private final Bootstrap bootstrap;
    private boolean shuttingDown;
    
    public NettyClient(Logger logger, PacketRegistry packetRegistry) {
        this.packetRegistry = packetRegistry;
        this.logger = logger;
        this.bootstrap = new Bootstrap();
    }
    
    @Override
    public void connect(String address, int port) {
        this.bootstrap
                .channel(NettyUtil.getChannel())
                .group(NettyUtil.getWorkerLoopGroup())
                .handler(new ClientChannelInitializer(this))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .remoteAddress(address, port);
        this.callbackTickFuture = NettyUtil.getWorkerLoopGroup().scheduleWithFixedDelay(this::callbackTick, 50L, 50L, TimeUnit.MILLISECONDS);
        connect();
    }

    private void connect() {
        this.bootstrap
                .connect()
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        logger.log(Level.INFO, "Connected to the server!");
                    } else {
                        logger.log(Level.WARNING, "Could not connect to the server. Reconnecting in 10 seconds..", future.cause());
                        future.channel().eventLoop().schedule((Runnable) this::connect, 10L, TimeUnit.SECONDS);
                    }
                });
    }
    
    @Override
    public void disconnect() {
        this.shuttingDown = true;
        if (this.connection != null) {
            callbackTickFuture.cancel(false);
            callbackTickFuture = null;
            this.connection.getContext().channel().close().syncUninterruptibly();
            NettyUtil.shutdownLoopGroups();
            this.connection = null;
        }
    }
    
    @Override
    public PacketRegistry getPacketRegistry() {
        return this.packetRegistry;
    }
    
    @Override
    public NettyConnection getConnection() {
        return this.connection;
    }
    
    @Override
    public void sendPacket(Packet packet) {
        if (this.connection == null)
            return;
        this.connection.getContext()
            .writeAndFlush(packet)
            .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
    
    public Logger getLogger() {
        return this.logger;
    }
    
    NettyConnection createConnection(ChannelHandlerContext ctx, NettyClientPacketHandler handler) {
        this.connection = new NettyConnection(ctx, handler);
        try {
            onConnected();
        } catch (Exception ex) {
            new Exception("Can not process connection callback", ex).printStackTrace();
        }
        return this.connection;
    }
    
    void deleteConnection(ChannelHandlerContext ctx) {
        this.connection = null;
        try {
            onDisconnected();
        } catch (Exception ex) {
            new Exception("Can not process disconnection callback", ex).printStackTrace();
        }
        if(!this.shuttingDown)
            connect();
    }
    
}
