package com.github.sy.server;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * on 2018/4/20.
 */
public class RequestBuffer {
    public static int TOTAL_BUFFER_SIZE = 10;
    int totalBytes = 0;
    ByteBuffer curBuffer = ByteBuffer.allocate(TOTAL_BUFFER_SIZE);
    List<Byte> totalBuffer = new ArrayList<>();

    public void clear() {
        totalBytes = 0;
        curBuffer.clear();
        totalBuffer.clear();
    }

    public boolean hadRemain(int curRead) {
        int capacity = curRead;
        if (totalBytes == 0) {
            curBuffer.rewind();
            curBuffer.get(); // skip type
            totalBytes = curBuffer.getInt();
            if (totalBytes <= capacity) {
                curBuffer.rewind();
                return false;
            }
        }

        if (totalBuffer.size() + capacity < totalBytes) {
            byte[] bytes = curBuffer.array();
            for (int i = 0; i < capacity; ++i) {
                totalBuffer.add(bytes[i]);
            }
            curBuffer.clear();
            return true;
        }
        curBuffer.flip();
        return false;
    }

    public ByteBuffer toByteBuffer() {
        if (totalBuffer.size() == 0) {
            return curBuffer;
        }
        ByteBuffer finalByteBuffer = ByteBuffer.allocate(totalBuffer.size() + curBuffer.limit());
        for (byte b : totalBuffer) {
            finalByteBuffer.put(b);
        }
        finalByteBuffer.put(curBuffer);
        finalByteBuffer.rewind();
        return finalByteBuffer;
    }

    public static void main(String[] a) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        System.err.println(byteBuffer.array());
        System.err.println(byteBuffer.limit());
    }
}
