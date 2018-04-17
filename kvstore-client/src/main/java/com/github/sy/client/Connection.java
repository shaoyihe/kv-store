package com.github.sy.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * on 2018/4/17.
 */
public class Connection implements Closeable {
    private final SocketChannel client;
    private final int DATA_SIZE_IN_BYTES = 4;
    private final byte GET_ACTION = 0;
    private final byte SET_ACTION = 1;

    public Connection(String serverAddress, int serverPort) throws IOException {
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8190);
        client = SocketChannel.open(hostAddress);
    }

    public String get(String key) throws IOException {
        byte[] keyBytes = key.getBytes();
        ByteBuffer byteBuffer = allocate(DATA_SIZE_IN_BYTES + keyBytes.length);
        byteBuffer.put(GET_ACTION);
        byteBuffer.putInt(keyBytes.length);
        byteBuffer.put(keyBytes);
        client.write(byteBuffer);
        return null;
    }

    public boolean put(String key, String val) {
        byte[] keyBytes = key.getBytes();
        byte[] valBytes = val.getBytes();
        ByteBuffer byteBuffer = allocate(DATA_SIZE_IN_BYTES * 2 + keyBytes.length + valBytes.length);
        byteBuffer.put(SET_ACTION);
        byteBuffer.putInt(keyBytes.length);
        byteBuffer.put(keyBytes);
        byteBuffer.putInt(valBytes.length);
        byteBuffer.put(valBytes);
        return true;
    }

    private ByteBuffer allocate(int dataSize) {
        return ByteBuffer.allocate(1 + dataSize);
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
