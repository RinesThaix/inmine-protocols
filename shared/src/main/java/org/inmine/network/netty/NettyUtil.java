package org.inmine.network.netty;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ThreadFactory;

/**
 * Created by RINES on 17.11.17.
 */
public class NettyUtil {
    private static EventLoopGroup bossLoopGroup = null;
    private static EventLoopGroup workerLoopGroup = null;
    
    public static boolean isUsed() {
        return bossLoopGroup != null;
    }
    
    public synchronized static void shutdownLoopGroups() {
        if (bossLoopGroup != null) {
            bossLoopGroup.shutdownGracefully();
            bossLoopGroup = null;
        }
        if (workerLoopGroup != null) {
            workerLoopGroup.shutdownGracefully();
            workerLoopGroup = null;
        }
    }
    
    public synchronized static EventLoopGroup getBossLoopGroup(boolean bossIsWorker, int bossThreads) {
        if (bossLoopGroup == null) {
            ThreadFactory tf;
            if (bossIsWorker)
                tf = new NamedThreadFactory("Netty IO Thread #", false);
            else
                tf = new NamedThreadFactory("Netty Boss Thread #", false);
            bossLoopGroup = newEventLoopGroup(bossThreads, tf);
        }
        return bossLoopGroup;
    }
    
    public synchronized static EventLoopGroup getWorkerLoopGroup(boolean bossIsWorker, int workerThreads) {
        if (workerLoopGroup == null) {
            if (bossIsWorker) {
                workerLoopGroup = bossLoopGroup;
            } else {
                ThreadFactory tf = new NamedThreadFactory("Netty Worker Thread #", false);
                workerLoopGroup = newEventLoopGroup(workerThreads, tf);
            }
        }
        return workerLoopGroup;
    }
    
    public static Class<? extends SocketChannel> getChannel() {
        return Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class;
    }
    
    public static Class<? extends ServerChannel> getServerChannel() {
        return Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }
    
    private static EventLoopGroup newEventLoopGroup(int threads, ThreadFactory tf) {
        return Epoll.isAvailable() ? new EpollEventLoopGroup(threads, tf) : new NioEventLoopGroup(threads, tf);
    }
}
