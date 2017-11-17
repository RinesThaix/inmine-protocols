package org.inmine.network.netty.client;

import io.netty.channel.ChannelHandlerContext;

import org.inmine.network.Packet;
import org.inmine.network.netty.NettyPacketHandler;
import org.inmine.network.packet.SPacketDisconnect;

/**
 * Created by RINES on 17.11.17.
 */
public class NettyClientPacketHandler extends NettyPacketHandler {
    
    private final NettyClient client;
    
    public NettyClientPacketHandler(NettyClient client) {
        this.client = client;
        this.clearHandlers();
    }
    
    private void handleDisconnect(SPacketDisconnect packet) {
        client.getLogger().info("Disconnected from server" + (packet.message == null ? "" : ". Message: " + packet.message));
        client.disconnect();
    }
    
    @Override
    public void clearHandlers() {
        super.clearHandlers();
        this.addHandler(SPacketDisconnect.class, this::handleDisconnect);
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
