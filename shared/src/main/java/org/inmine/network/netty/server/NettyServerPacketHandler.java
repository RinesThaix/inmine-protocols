package org.inmine.network.netty.server;

import io.netty.channel.ChannelHandlerContext;

import org.inmine.network.Packet;
import org.inmine.network.netty.NettyPacketHandler;
import org.inmine.network.packet.SPacketHandshake;

/**
 * Created by RINES on 17.11.17.
 */
public class NettyServerPacketHandler extends NettyPacketHandler {
    
    private final NettyServer server;
    
    public NettyServerPacketHandler(NettyServer server) {
        this.server = server;
        this.addHandler(SPacketHandshake.class, this::handshake);
    }
    
    private void handshake(SPacketHandshake packet) {
        if (packet.version > this.server.getPacketRegistry().getVersion()) {
            this.connection.disconnect("Are you from future?");
        } else if (packet.version < this.server.getPacketRegistry().getVersion()) {
            this.connection.disconnect("Client protocol version is outdated");
        }
        this.removeHandler(SPacketHandshake.class, this::handshake);
        try {
            server.onNewConnection(connection);
        } catch (Exception ex) {
            new Exception("Can not process connection callback", ex).printStackTrace();
        }
    }
    
    @Override
    public void handle(Packet packet) {
        this.server.onPacketPreReceived(packet);
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
