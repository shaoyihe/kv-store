package com.github.sy.server;

import java.util.HashMap;
import java.util.Map;

/**
 * on 2018/4/17.
 */
public class Store {
    private Map<Key, byte[]> store = new HashMap<>();

    public byte[] get(byte[] key) {
        return store.get(new Key(key));
    }

    public byte[] put(byte[] key, byte[] val) {
        return store.put(new Key(key), val);
    }

    public static final Store INSTANCE = new Store();
}
