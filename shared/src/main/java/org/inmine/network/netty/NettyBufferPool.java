package org.inmine.network.netty;

import io.netty.buffer.ByteBuf;

import java.util.LinkedList;

/**
 * @author xtrafrancyz
 */
class NettyBufferPool {
    private final int maxIdle;
    private final LinkedList<NettyBuffer> free;
    
    public NettyBufferPool(int maxIdle) {
        this.maxIdle = maxIdle;
        this.free = new LinkedList<>();
    }
    
    public NettyBuffer wrap(ByteBuf nettyBuffer) {
        synchronized (free) {
            NettyBuffer buf = free.poll();
            if (buf != null) {
                buf.setHandle(nettyBuffer);
                return buf;
            }
        }
        return new NettyBuffer(this, nettyBuffer);
    }
    
    public void release(NettyBuffer buf) {
        buf.setHandle(null);
        synchronized (free) {
            if (free.size() < maxIdle)
                free.add(buf);
        }
    }
}
