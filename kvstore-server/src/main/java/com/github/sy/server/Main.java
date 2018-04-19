package com.github.sy.server;

/**
 * on 2018/4/17.
 */
public class Main {
    public static void main(String[] a) throws Exception {
        new Bootstrap("localhost", 8036).startServer();
        Thread.currentThread().join();
    }
}
