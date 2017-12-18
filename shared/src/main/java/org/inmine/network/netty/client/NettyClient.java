package org.inmine.network.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.util.concurrent.ScheduledFuture;

import org.inmine.network.NetworkClient;
import org.inmine.network.Packet;
import org.inmine.network.PacketRegistry;
import org.inmine.network.callback.CallbackHandler;
import org.inmine.network.netty.NettyConnection;
import org.inmine.network.netty.NettyPacketHandler;
import org.inmine.network.netty.NettyUtil;
import org.inmine.network.packet.SPacketHandshake;
import org.inmine.network.packet.SPacketKeepAlive;

import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by RINES on 17.11.17.
 */
public abstract class NettyClient extends CallbackHandler implements NetworkClient {

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
    Consumer<Packet> packetReceivedListener;
    Consumer<Packet> packetSentListener;

    public NettyClient(Logger logger, PacketRegistry packetRegistry) {
        this.packetRegistry = packetRegistry;
        this.logger = logger;
    }

    @Override
    public void connect(String address, int port) {
        this.shuttingDown = false;
        this.logReconnectMessage = true;
        if (this.bootstrap == null) {
            this.bootstrap = new Bootstrap()
                .channel(NettyUtil.getChannel())
                .group(NettyUtil.getWorkerLoopGroup())
                .handler(new ClientChannelInitializer(this))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        }
        this.bootstrap.remoteAddress(address, port);
        connect();
    }

    private void connect() {
        this.reconnectFuture = null;
        if (this.callbackTickFuture == null)
            this.callbackTickFuture = NettyUtil.getWorkerLoopGroup().scheduleWithFixedDelay(this::callbackTick, 50L, 50L, TimeUnit.MILLISECONDS);
        if (this.keepAliveFuture == null)
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
                    sendPacket(new SPacketKeepAlive());
                }
            }, 1L, 1L, TimeUnit.SECONDS);
        this.connectFuture = this.bootstrap
            .connect()
            .addListener((ChannelFutureListener) future -> {
                this.connectFuture = null;
                if (future.isSuccess()) {
                    this.logger.log(Level.INFO, "Connected to the server");
                    this.logReconnectMessage = true;
                } else {
                    if (this.logReconnectMessage) {
                        this.logger.log(Level.WARNING, "Could not connect to the server. I will connect as soon as the server is online..", future.cause());
                        this.logReconnectMessage = false;
                    }
                    if (!(future.cause() instanceof CancellationException))
                        this.reconnectFuture = future.channel().eventLoop().schedule((Runnable) this::connect, 10L, TimeUnit.SECONDS);
                }
            });
    }

    @Override
    public void disconnect() {
        this.shuttingDown = true;
        if (this.connectFuture != null) {
            this.connectFuture.cancel(true);
            this.connectFuture = null;
        }
        if (this.reconnectFuture != null) {
            this.reconnectFuture.cancel(true);
            this.reconnectFuture = null;
        }
        if (this.callbackTickFuture != null) {
            this.callbackTickFuture.cancel(false);
            this.callbackTickFuture = null;
        }
        super.callCallbacksTimeouts();
        if (this.keepAliveFuture != null) {
            this.keepAliveFuture.cancel(false);
            this.keepAliveFuture = null;
        }
        if (this.connection != null) {
            this.connection.getContext().channel().close().syncUninterruptibly();
            this.connection = null;
        }
        this.logger.info("Disconnected from the server");
    }

    @Override
    public boolean isConnected() {
        return this.connection != null;
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
        this.connection.sendPacket(packet);
    }

    @Override
    public void setPacketReceivedListener(Consumer<Packet> listener) {
        this.packetReceivedListener = listener;
    }

    @Override
    public void setPacketSentListener(Consumer<Packet> listener) {
        this.packetSentListener = listener;
    }

    public Logger getLogger() {
        return this.logger;
    }

    NettyConnection createConnection(ChannelHandlerContext ctx, NettyClientPacketHandler handler) {
        this.connection = new NettyConnection(this, ctx, handler);
        try {
            sendPacket(new SPacketHandshake(getPacketRegistry().getVersion()));
        } catch (Exception ex) {
            new Exception("Can not process connection callback", ex).printStackTrace();
        }
        return this.connection;
    }

    void deleteConnection(ChannelHandlerContext ctx) {
        this.connection.getHandler().setConnection(null);
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
