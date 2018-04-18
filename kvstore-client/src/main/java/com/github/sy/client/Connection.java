package com.github.sy.client;

import com.github.sy.common.ByteBufferUtil;
import com.github.sy.common.ResponseType;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * on 2018/4/17.
 */
public class Connection implements Closeable, ResponseType {
    private final SocketChannel client;

    public Connection(String serverAddress, int serverPort) throws IOException {
        InetSocketAddress hostAddress = new InetSocketAddress(serverAddress, serverPort);
        client = SocketChannel.open(hostAddress);
    }

    public String get(String key) throws IOException {
        client.write(ByteBufferUtil.allocateWith(GET_ACTION, key));
        ByteBuffer response = ByteBuffer.allocate(1024);
        client.read(response);
        response.flip();
        byte ok = response.get();
        if (ok == GET_NO_VAL) {
            return null;
        }
        return new String(ByteBufferUtil.readSeq(response));
    }

    public String del(String key) throws IOException {
        client.write(ByteBufferUtil.allocateWith(DEL_ACTION, key));
        ByteBuffer response = ByteBuffer.allocate(1024);
        client.read(response);
        response.flip();
        byte ok = response.get();
        if (ok == DEL_NO_VAL) {
            return null;
        }
        return new String(ByteBufferUtil.readSeq(response));
    }

    public String put(String key, String val) throws IOException {
        client.write(ByteBufferUtil.allocateWith(SET_ACTION, key, val));
        ByteBuffer response = ByteBuffer.allocate(1024);
        client.read(response);
        response.flip();
        byte ok = response.get();
        if (ok == SET_WITH_NO_VAL) {
            return null;
        }
        return new String(ByteBufferUtil.readSeq(response));
    }


    private ByteBuffer allocate(int dataSize) {
        return ByteBuffer.allocate(RESPONSE_TYPE_IN_BYTES + dataSize);
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
