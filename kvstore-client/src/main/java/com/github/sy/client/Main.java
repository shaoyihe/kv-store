package com.github.sy.client;

import java.io.IOException;

/**
 * on 2018/4/17.
 */
public class Main {
    public static void main(String[] a) throws Exception {
        Connection connection = new Connection("localhost", 8035);
        new Thread(() -> {
            try {
                System.err.println(connection.put("test1", "test1"));
                System.err.println(connection.get("test"));
                System.err.println(connection.get("test2"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                System.err.println(connection.put("test2", "test2"));
                System.err.println(connection.get("test1"));
                System.err.println(connection.get("test2"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.currentThread().join();

    }
}
