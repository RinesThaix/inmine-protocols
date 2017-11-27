package org.inmine.network.netty;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;

import org.inmine.network.PacketRegistry;

import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Created by RINES on 17.11.17.
 */
public class ChannelInitializer<T extends Channel> extends io.netty.channel.ChannelInitializer<T> {

    private final PacketEncoder encoder;
    private final PacketRegistry packetRegistry;
    private final Supplier<NettyPacketHandler> packetHandlerGenerator;
    private final Logger logger;

    public ChannelInitializer(PacketRegistry packetRegistry, Supplier<NettyPacketHandler> packetHandlerGenerator, Logger logger) {
        this.packetRegistry = packetRegistry;
        this.packetHandlerGenerator = packetHandlerGenerator;
        this.logger = logger;
        this.encoder = new PacketEncoder();
    }

    @Override
    protected void initChannel(T ch) throws Exception {
        ch.config().setAllocator(PooledByteBufAllocator.DEFAULT);

        ch.pipeline().addLast("packet-encoder", this.encoder);
        ch.pipeline().addLast("packet-decoder", new PacketDecoder(this.packetRegistry));
        HandlerBoss boss = new HandlerBoss(this.packetHandlerGenerator.get(), this.logger);
        ch.pipeline().addLast("boss", boss);
        ch.attr(HandlerBoss.BOSS_KEY).set(boss);
    }

}
