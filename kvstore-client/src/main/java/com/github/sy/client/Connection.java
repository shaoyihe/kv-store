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
        InetSocketAddress hostAddress = new InetSocketAddress(serverAddress, serverPort);
        client = SocketChannel.open(hostAddress);
    }

    public String get(String key) throws IOException {
        byte[] keyBytes = key.getBytes();
        ByteBuffer byteBuffer = allocate(DATA_SIZE_IN_BYTES + keyBytes.length);
        byteBuffer.put(GET_ACTION);
        byteBuffer.putInt(keyBytes.length);
        byteBuffer.put(keyBytes);
        byteBuffer.flip();
        client.write(byteBuffer);
        ByteBuffer response = ByteBuffer.allocate(1024);
        client.read(response);
        response.flip();
        byte ok = response.get();
        if (ok == 0) {
            return null;
        }
        return new String(readSeq(response));
    }

    public boolean put(String key, String val) throws IOException {
        byte[] keyBytes = key.getBytes();
        byte[] valBytes = val.getBytes();
        ByteBuffer byteBuffer = allocate(DATA_SIZE_IN_BYTES * 2 + keyBytes.length + valBytes.length);
        byteBuffer.put(SET_ACTION);
        byteBuffer.putInt(keyBytes.length);
        byteBuffer.put(keyBytes);
        byteBuffer.putInt(valBytes.length);
        byteBuffer.put(valBytes);
        byteBuffer.flip();
        client.write(byteBuffer);
        ByteBuffer response = ByteBuffer.allocate(1024);
        client.read(response);
        response.flip();
        byte ok = response.get();
        if (ok == 0) {
            return true;
        }
        return false;
    }

    byte[] readSeq(ByteBuffer buffer) {
        int keySize = buffer.getInt();
        byte[] val = new byte[keySize];
        buffer.get(val);
        return val;
    }

    private ByteBuffer allocate(int dataSize) {
        return ByteBuffer.allocate(1 + dataSize);
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
