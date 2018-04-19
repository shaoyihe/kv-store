package com.github.sy.common;

import java.nio.ByteBuffer;

import static com.github.sy.common.ResponseType.DATA_SIZE_IN_BYTES;

/**
 * on 2018/4/18.
 */
public class ByteBufferUtil {
    public static byte[] readSeq(ByteBuffer buffer) {
        int keySize = buffer.getInt();
        byte[] val = new byte[keySize];
        buffer.get(val);
        return val;
    }

    public static void putSeq(ByteBuffer buffer, byte[]... vals) {
        for (byte[] val : vals) {
            buffer.putInt(val.length);
            buffer.put(val);
        }
    }

    public static ByteBuffer allocateWith(byte type, String... vals) {
        byte[][] bytes = new byte[vals.length][];
        for (int i = 0; i < vals.length; ++i) {
            bytes[i] = vals[i].getBytes();
        }
        return allocateWithBytes(type, bytes);
    }

    public static ByteBuffer allocateWithBytes(byte type, byte[]... bytes) {
        int totalValBytes = 0;
        for (byte[] aByte : bytes) {
            totalValBytes += aByte.length;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(1 + DATA_SIZE_IN_BYTES * bytes.length + totalValBytes);
        byteBuffer.put(type);
        putSeq(byteBuffer, bytes);
        byteBuffer.flip();
        return byteBuffer;
    }
}
