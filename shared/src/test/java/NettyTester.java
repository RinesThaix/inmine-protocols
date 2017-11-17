import org.inmine.network.Buffer;
import org.inmine.network.Packet;
import org.inmine.network.PacketRegistry;
import org.inmine.network.netty.NettyConnection;
import org.inmine.network.netty.client.NettyClient;
import org.inmine.network.netty.server.NettyServer;
import org.inmine.util.OwnLogger;

import java.util.Random;

/**
 * Created by RINES on 17.11.17.
 */
public class NettyTester {
    
    public static void main(String[] args) {
        OwnLogger logger = new OwnLogger("test");
        TestPacketRegistry registry = new TestPacketRegistry();
        NettyServer server = new NettyServer(logger, registry) {

            private boolean closed;
            private int a = 0;

            @Override
            public void onNewConnection(NettyConnection connection) {
                logger.info("Server has just received the client!");
                connection.getHandler().addHandler(PacketTest.class, p -> {
                    if(!closed) {
                        closed = true;
                        connection.getContext().close().syncUninterruptibly();
                        logger.info("DISCONNECTING");
                        return;
                    }
                    logger.info("Server received packet with id " + p.id);
                    ++p.id;
                    p.random = String.valueOf(new Random().nextInt());
                    sendPacket(connection, p.id == 10 ? new PacketDisconnect() : p);
                });
            }
            
            @Override
            public void onDisconnecting(NettyConnection connection) {
                if(++a < 2)
                    return;
                logger.info("Stopping the server..");
                stop();
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
                getConnection().getHandler().addHandler(PacketDisconnect.class, p -> {
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
            }
        };
        server.start("localhost", 8940);
        client.connect("localhost", 8940);
        client.disconnect();
    }
    
    private static class TestPacketRegistry extends PacketRegistry {
        
        private TestPacketRegistry() {
            super(PacketTest::new, PacketDisconnect::new);
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
    
    public static class PacketDisconnect extends Packet {
        
        @Override
        public int getId() {
            return 2;
        }
        
        @Override
        public void write(Buffer buffer) {
            
        }
        
        @Override
        public void read(Buffer buffer) {
            
        }
        
    }
    
}
