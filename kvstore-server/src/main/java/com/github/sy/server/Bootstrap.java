package com.github.sy.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * on 2018/4/17.
 */
public class Bootstrap implements Runnable {
    private volatile Selector selector;
    private Map<SocketChannel, RequestBuffer> dataMapper;
    private final InetSocketAddress listenAddress;

    public Bootstrap(String address, int port) {
        listenAddress = new InetSocketAddress(address, port);
        dataMapper = new HashMap<>();
    }

    public void startServer() throws Exception {
        this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        // retrieve server socket and bind to port
        serverChannel.socket().bind(listenAddress);
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        L.log.info("Server started...");

        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                // wait for events
                this.selector.select();

                //work on selected keys
                Iterator keys = this.selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = (SelectionKey) keys.next();
                    keys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable()) {
                        this.accept(key);
                    } else if (key.isReadable()) {
                        this.read(key);
                    } else if (key.isWritable()) {
                        this.write(key);
                    }
                }
            } catch (Exception e) {
                L.log.error("start error", e);
            }
        }
    }

    //accept a connection made to this channel's socket
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        L.log.info("Connected to: " + remoteAddr);

        dataMapper.put(channel, new RequestBuffer());
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    //read from the socket channel
    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        RequestBuffer buffer = dataMapper.get(channel);
        int numRead = -1;
        try {
            numRead = channel.read(buffer.curBuffer);
        } catch (Exception e) {
            L.log.error("read error", e);
        }

        if (numRead == -1) {
            this.dataMapper.remove(channel);
            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            L.log.error("Connection closed by client: " + remoteAddr);
            channel.close();
            key.cancel();
            return;
        } else {
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = dataMapper.get(channel).curBuffer;
//        ByteBuffer buffer = dataMapper.get(channel);
        buffer.flip();
        ByteBuffer response = Action.processRequest(buffer);
        channel.write(response);
        buffer.clear();
        key.interestOps(SelectionKey.OP_READ);
    }

    private String toStr(ByteBuffer req) {
        int len = req.limit();
        if (req.position() > 0) {
            req.flip();
        }
        byte[] b = new byte[len];
        req.put(b);
        req.flip();
        return new String(b);
    }

}
