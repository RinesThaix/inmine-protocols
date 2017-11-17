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
    
    private final ByteBuf buffer;
    
    public NettyBuffer(ByteBuf buffer) {
        this.buffer = buffer;
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
    
    public ByteBuf getByteBuf() {
        return this.buffer;
    }
    
}
