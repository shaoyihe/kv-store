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
        int limit = curBuffer.limit();
        if (totalBytes == 0) {
            totalBytes = curBuffer.getInt();
            if (totalBytes <= limit) {
                curBuffer.flip();
                return true;
            }
        }
        if (totalBuffer.size() + limit <= totalBytes) {
            return true;
        }
        byte[] bytes = curBuffer.array();
        for (int i = 0; i < limit; ++i) {
            totalBuffer.add(bytes[i]);
        }
        curBuffer.clear();
        return false;
    }

    public ByteBuffer toByteBuffer() {
        if (totalBuffer.size() == 0) {
            return curBuffer;
        }
        ByteBuffer finalByteBuffer = ByteBuffer.allocate(totalBuffer.size() + curBuffer.limit() - 1);
        return null;
    }

    public static void main(String[] a) {
        ByteBuffer byteBuffer =ByteBuffer.allocate(10);
        System.err.println(byteBuffer.array());
        System.err.println(byteBuffer.limit());
    }
}
