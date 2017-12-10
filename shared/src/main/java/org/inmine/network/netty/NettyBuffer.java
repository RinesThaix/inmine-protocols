package org.inmine.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;

import org.inmine.network.Buffer;

import java.nio.charset.StandardCharsets;

/**
 * Created by RINES on 17.11.17.
 */
public class NettyBuffer extends Buffer {

    private final NettyBufferPool pool;
    private ByteBuf buffer;
    private boolean releaseNetty = false;

    public NettyBuffer(ByteBuf buffer) {
        this(null, buffer);
    }

    NettyBuffer(NettyBufferPool pool, ByteBuf buffer) {
        this.pool = pool;
        this.buffer = buffer;
    }

    public void setHandle(ByteBuf buffer) {
        if (this.buffer != null && releaseNetty) {
            releaseNetty = false;
            this.buffer.release();
        }
        if (buffer == null)
            releaseNetty = false;
        this.buffer = buffer;
    }

    public ByteBuf getHandle() {
        return this.buffer;
    }

    @Override
    public void release() {
        if (pool != null)
            pool.release(this);
    }

    @Override
    public byte readByte() {
        return this.buffer.readByte();
    }

    @Override
    public void writeByte(byte val) {
        this.buffer.writeByte(val);
    }

    @Override
    public short readShort() {
        return this.buffer.readShort();
    }

    @Override
    public void writeShort(short val) {
        this.buffer.writeShort(val);
    }

    @Override
    public int readInt() {
        return this.buffer.readInt();
    }

    @Override
    public void writeInt(int val) {
        this.buffer.writeInt(val);
    }

    @Override
    public long readLong() {
        return this.buffer.readLong();
    }

    @Override
    public void writeLong(long val) {
        this.buffer.writeLong(val);
    }

    @Override
    public float readFloat() {
        return this.buffer.readFloat();
    }

    @Override
    public void writeFloat(float val) {
        this.buffer.writeFloat(val);
    }

    @Override
    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        this.buffer.readBytes(bytes);
        return bytes;
    }

    @Override
    public void writeBytes(byte[] bytes) {
        this.buffer.writeBytes(bytes);
    }

    @Override
    public String readString(int maxLength) {
        int length = readVarInt();
        if (length > maxLength)
            throw new IllegalStateException("Trying to read string with length " + length + " when the max is " + maxLength);
        String read = this.buffer.toString(this.buffer.readerIndex(), length, StandardCharsets.UTF_8);
        this.buffer.skipBytes(length);
        return read;
    }

    @Override
    public void writeString(String s) {
        ByteBuf encoded = PooledByteBufAllocator.DEFAULT.buffer(ByteBufUtil.utf8MaxBytes(s));
        try {
            ByteBufUtil.writeUtf8(encoded, s);
            writeVarInt(encoded.readableBytes());
            this.buffer.writeBytes(encoded);
        } finally {
            encoded.release();
        }
    }

    @Override
    public NettyBuffer newBuffer(int size) {
        ByteBuf buf = buffer.alloc().buffer(size);
        NettyBuffer wrapped;
        if (pool != null)
            wrapped = this.pool.wrap(buf);
        else
            wrapped = new NettyBuffer(buf);
        wrapped.releaseNetty = true;
        return wrapped;
    }

}
