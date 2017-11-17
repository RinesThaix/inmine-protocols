package org.inmine.network.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;

import org.inmine.network.Connection;
import org.inmine.network.NetworkServer;
import org.inmine.network.PacketRegistry;
import org.inmine.network.callback.CallbackHandler;
import org.inmine.network.netty.NettyConnection;
import org.inmine.network.netty.NettyPacketHandler;
import org.inmine.network.netty.NettyUtil;
import org.inmine.network.packet.SPacketHandshake;
import org.inmine.network.packet.SPacketKeepAlive;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by RINES on 17.11.17.
 */
public abstract class NettyServer extends CallbackHandler implements NetworkServer {
    
    private final PacketRegistry packetRegistry;
    private final Logger logger;
    
    private final Map<SocketAddress, NettyConnection> connections = new ConcurrentHashMap<>();
    
    private ScheduledFuture<?> callbackTickFuture = null;
    private ScheduledFuture<?> keepAliveFuture = null;
    
    public NettyServer(Logger logger, PacketRegistry packetRegistry) {
        this.packetRegistry = packetRegistry;
        this.logger = logger;
    }
    
    private String address;
    private int port;
    
    private Channel channel;
    
    @Override
    public void start(String address, int port) {
        this.address = address;
        this.port = port;
        ServerBootstrap b = new ServerBootstrap()
            .group(NettyUtil.getBossLoopGroup(), NettyUtil.getWorkerLoopGroup())
            .channel(NettyUtil.getServerChannel())
            .childHandler(new ServerChannelInitializer(this));
        b.bind(address, port).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                channel = future.channel();
                logger.info("TCP listening on " + address + ":" + port);
            } else {
                logger.log(Level.WARNING, "Could not bind to host " + address + ":" + port, future.cause());
            }
        });
        if (callbackTickFuture == null)
            callbackTickFuture = NettyUtil.getWorkerLoopGroup().scheduleWithFixedDelay(this::callbackTick, 50L, 50L, TimeUnit.MILLISECONDS);
        if (keepAliveFuture == null)
            this.keepAliveFuture = NettyUtil.getWorkerLoopGroup().scheduleWithFixedDelay(() -> {
                long current = System.currentTimeMillis();
                this.connections.values().forEach(connection -> {
                    NettyPacketHandler handler = connection.getHandler();
                    if (current - handler.getLastPacketReceivedTime() > 40000L) {
                        connection.disconnect();
                        return;
                    }
                    if (current - handler.getLastPacketSentTime() > 30000L) {
                        connection.sendPacket(new SPacketKeepAlive());
                    }
                });
            }, 5L, 5L, TimeUnit.SECONDS);
    }
    
    @Override
    public void stop() {
        if (callbackTickFuture != null) {
            callbackTickFuture.cancel(false);
            callbackTickFuture = null;
        }
        if (keepAliveFuture != null) {
            keepAliveFuture.cancel(false);
            keepAliveFuture = null;
        }
        super.callCallbacksTimeouts();
        if (channel != null) {
            logger.info("Closing tcp listener");
            channel.close().syncUninterruptibly();
        }
    }
    
    @Override
    public String getAddress() {
        return this.address;
    }
    
    @Override
    public int getBindPort() {
        return this.port;
    }
    
    @Override
    public Collection<NettyConnection> getConnections() {
        return this.connections.values();
    }
    
    @Override
    public PacketRegistry getPacketRegistry() {
        return this.packetRegistry;
    }
    
    @Override
    public final void onNewConnection(Connection connection) {
        onNewConnection((NettyConnection) connection);
    }
    
    @Override
    public final void onDisconnecting(Connection connection) {
        onDisconnecting((NettyConnection) connection);
    }
    
    public abstract void onNewConnection(NettyConnection connection);
    
    public abstract void onDisconnecting(NettyConnection connection);
    
    public Logger getLogger() {
        return this.logger;
    }
    
    NettyConnection createNewConnection(ChannelHandlerContext ctx, NettyServerPacketHandler handler) {
        NettyConnection connection = new NettyConnection(this, ctx, handler);
        this.connections.put(ctx.channel().remoteAddress(), connection);
        connection.getHandler().addHandler(SPacketHandshake.class, handshake -> {
            if (handshake.version > getPacketRegistry().getVersion()) {
                connection.disconnect("Are you from future?");
            } else if (handshake.version < getPacketRegistry().getVersion()) {
                connection.disconnect("Client protocol version is outdated");
            }
        });
        try {
            onNewConnection(connection);
        } catch (Exception ex) {
            new Exception("Can not process connection callback", ex).printStackTrace();
        }
        return connection;
    }
    
    void deleteConnection(ChannelHandlerContext ctx) {
        NettyConnection connection = this.connections.remove(ctx.channel().remoteAddress());
        if (connection == null)
            return;
        try {
            connection.getHandler().setConnection(null);
            onDisconnecting(connection);
        } catch (Exception ex) {
            new Exception("Can not process disconnection callback", ex).printStackTrace();
        }
    }
    
}
