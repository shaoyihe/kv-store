package com.github.sy.server;

import java.util.Arrays;

/**
 * on 2018/4/17.
 */
public class Key {
    private byte[] val;

    public Key(byte[] val) {
        this.val = val;
    }

    public byte[] getVal() {
        return val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key value = (Key) o;
        return Arrays.equals(val, value.val);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(val);
    }
}
