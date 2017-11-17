package org.inmine.network.netty.server;

import io.netty.channel.ChannelHandlerContext;

import org.inmine.network.Connection;
import org.inmine.network.Packet;
import org.inmine.network.netty.NettyPacketHandler;

/**
 * Created by RINES on 17.11.17.
 */
public class NettyServerPacketHandler extends NettyPacketHandler {
    
    private final NettyServer server;
    
    public NettyServerPacketHandler(NettyServer server) {
        this.server = server;
    }
    
    @Override
    public void handle(Packet packet) {
        Connection connection = getConnection();
        this.server.onPacketPreReceived(connection, packet);
        super.handle(packet);
    }
    
    @Override
    public void onConnect(ChannelHandlerContext ctx) {
        setConnection(this.server.createNewConnection(ctx, this));
    }
    
    @Override
    public void onDisconnect(ChannelHandlerContext ctx) {
        this.server.deleteConnection(ctx);
    }
    
}
