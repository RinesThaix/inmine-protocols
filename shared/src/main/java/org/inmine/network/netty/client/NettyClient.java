package org.inmine.network.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.util.concurrent.ScheduledFuture;

import org.inmine.network.Packet;
import org.inmine.network.PacketRegistry;
import org.inmine.network.callback.AbstractNetworkClient;
import org.inmine.network.netty.NettyConnection;
import org.inmine.network.netty.NettyPacketHandler;
import org.inmine.network.netty.NettyUtil;
import org.inmine.network.packet.Packet0KeepAlive;

import java.util.concurrent.CancellationException;
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
    
    private ChannelFuture connectFuture;
    private ScheduledFuture<?> reconnectFuture;
    private ScheduledFuture<?> keepAliveFuture;
    private Bootstrap bootstrap;
    private boolean shuttingDown;
    private boolean logReconnectMessage;
    
    public NettyClient(Logger logger, PacketRegistry packetRegistry) {
        this.packetRegistry = packetRegistry;
        this.logger = logger;
    }
    
    @Override
    public void connect(String address, int port) {
        this.shuttingDown = false;
        this.logReconnectMessage = true;
        if (bootstrap == null) {
            this.bootstrap = new Bootstrap()
                .channel(NettyUtil.getChannel())
                .group(NettyUtil.getWorkerLoopGroup())
                .handler(new ClientChannelInitializer(this))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        }
        if (callbackTickFuture == null)
            this.callbackTickFuture = NettyUtil.getWorkerLoopGroup().scheduleWithFixedDelay(this::callbackTick, 50L, 50L, TimeUnit.MILLISECONDS);
        if (keepAliveFuture == null)
            this.keepAliveFuture = NettyUtil.getWorkerLoopGroup().scheduleWithFixedDelay(() -> {
                if (this.connection == null)
                    return;
                long current = System.currentTimeMillis();
                NettyPacketHandler handler = this.connection.getHandler();
                if (current - handler.getLastPacketReceivedTime() > 40000L) {
                    disconnect();
                    this.shuttingDown = false;
                    connect();
                    return;
                }
                if (current - handler.getLastPacketSentTime() > 30000L) {
                    sendPacket(new Packet0KeepAlive());
                }
            }, 1L, 1L, TimeUnit.SECONDS);
        bootstrap.remoteAddress(address, port);
        connect();
    }
    
    private void connect() {
        reconnectFuture = null;
        connectFuture = this.bootstrap
            .connect()
            .addListener((ChannelFutureListener) future -> {
                connectFuture = null;
                if (future.isSuccess()) {
                    logger.log(Level.INFO, "Connected to the server!");
                    logReconnectMessage = true;
                } else {
                    if (logReconnectMessage) {
                        logger.log(Level.WARNING, "Could not connect to the server. I will connect as soon as the server is online..", future.cause());
                        logReconnectMessage = false;
                    }
                    if (!(future.cause() instanceof CancellationException))
                        reconnectFuture = future.channel().eventLoop().schedule((Runnable) this::connect, 10L, TimeUnit.SECONDS);
                }
            });
    }
    
    @Override
    public void disconnect() {
        this.shuttingDown = true;
        if (connectFuture != null) {
            connectFuture.cancel(true);
            connectFuture = null;
        }
        if (reconnectFuture != null) {
            reconnectFuture.cancel(true);
            reconnectFuture = null;
        }
        if (callbackTickFuture != null) {
            callbackTickFuture.cancel(false);
            callbackTickFuture = null;
        }
        super.callCallbacksTimeouts();
        if (keepAliveFuture != null) {
            keepAliveFuture.cancel(false);
            keepAliveFuture = null;
        }
        if (this.connection != null) {
            this.connection.getContext().channel().close().syncUninterruptibly();
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
        this.connection.getHandler().packetSent();
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
        if (!this.shuttingDown)
            connect();
    }
    
}
