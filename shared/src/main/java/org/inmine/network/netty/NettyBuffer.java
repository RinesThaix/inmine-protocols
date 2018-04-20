package org.inmine.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.Recycler;

import org.inmine.network.Buffer;

import java.nio.charset.StandardCharsets;

/**
 * Created by RINES on 17.11.17.
 */
public class NettyBuffer extends Buffer {
    private static Recycler<NettyBuffer> RECYCLER = new Recycler<NettyBuffer>() {
        @Override
        protected NettyBuffer newObject(Handle<NettyBuffer> handle) {
            return new NettyBuffer(handle);
        }
    };

    private final Recycler.Handle<NettyBuffer> recycler;
    private ByteBuf buffer;
    private boolean releaseNetty = false;

    private NettyBuffer(Recycler.Handle<NettyBuffer> recycler) {
        this.recycler = recycler;
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
        setHandle(null);
        recycler.recycle(this);
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
        ByteBuf encoded = buffer.alloc().buffer(ByteBufUtil.utf8MaxBytes(s));
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
        NettyBuffer wrapped = newInstance(buffer.alloc().buffer(size));
        wrapped.releaseNetty = true;
        return wrapped;
    }

    public static NettyBuffer newInstance() {
        return RECYCLER.get();
    }

    public static NettyBuffer newInstance(ByteBuf buffer) {
        NettyBuffer wrapper = RECYCLER.get();
        wrapper.setHandle(buffer);
        return wrapper;
    }
}
