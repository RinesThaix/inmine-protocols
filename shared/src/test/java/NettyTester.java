import org.inmine.network.Buffer;
import org.inmine.network.Packet;
import org.inmine.network.PacketRegistry;
import org.inmine.network.netty.NettyConnection;
import org.inmine.network.netty.NettyUtil;
import org.inmine.network.netty.client.NettyClient;
import org.inmine.network.netty.server.NettyServer;
import org.inmine.network.packet.SPacketDisconnect;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by RINES on 17.11.17.
 */
public class NettyTester {
    
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("test");
        TestPacketRegistry registry = new TestPacketRegistry();
        NettyServer server = new NettyServer(logger, registry) {
            
            private boolean closed;
            private int a = 0;
            
            @Override
            public void onNewConnection(NettyConnection connection) {
                logger.info("Server has just received the client!");
                connection.getHandler().addHandler(PacketTest.class, p -> {
                    if (!closed) {
                        closed = true;
                        connection.getContext().close().syncUninterruptibly();
                        logger.info("DISCONNECTING");
                        return;
                    }
                    logger.info("Server received packet with id " + p.id);
                    ++p.id;
                    p.random = String.valueOf(new Random().nextInt());
                    connection.sendPacket(p.id == 10 ? new SPacketDisconnect() : p);
                });
            }
            
            @Override
            public void onDisconnecting(NettyConnection connection) {
                if (++a < 2)
                    return;
                logger.info("Stopping the server..");
                stop();
                NettyUtil.shutdownLoopGroups();

                logger.info("[Server] Sent bytes " + getStatistics().getSentBytes());
                logger.info("[Server] Received bytes " + getStatistics().getReceivedBytes());
            }
        };
        NettyClient client = new NettyClient(logger, registry) {
            
            @Override
            public void onConnected() {
                logger.info("Client has just connected to the server!");
                getConnection().getHandler().addHandler(PacketTest.class, p -> {
                    logger.info("Client received packet with id " + p.id);
                    ++p.id;
                    p.random = String.valueOf(new Random().nextInt());
                    sendPacket(p);
                });
                getConnection().getHandler().addHandler(SPacketDisconnect.class, p -> {
                    logger.info("Client received disconnecting packet");
                    disconnect();
                });
                PacketTest packet = new PacketTest();
                packet.random = String.valueOf(new Random().nextInt());
                packet.id = 1;
                sendPacket(packet);
            }
            
            @Override
            public void onDisconnected() {
                logger.info("Disconnecting the client..");

                logger.info("[Client] Sent bytes " + getStatistics().getSentBytes());
                logger.info("[Client] Received bytes " + getStatistics().getReceivedBytes());
            }
        };
        server.start("localhost", 8940);
        client.connect("localhost", 8940);
    }
    
    private static class TestPacketRegistry extends PacketRegistry {
        
        private TestPacketRegistry() {
            super(1, PacketTest::new);
        }
        
    }
    
    public static class PacketTest extends Packet {
        
        private String random;
        private long id;
        
        @Override
        public int getId() {
            return 1;
        }
        
        @Override
        public void write(Buffer buffer) {
            buffer.writeVarLong(this.id);
            buffer.writeString(random);
        }
        
        @Override
        public void read(Buffer buffer) {
            this.id = buffer.readVarLong();
            this.random = buffer.readString(255);
        }
        
    }
}
