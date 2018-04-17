package com.github.sy.server;

import java.nio.ByteBuffer;

/**
 * on 2018/4/17.
 */
public class SetAction extends Action {
    @Override
    public ByteBuffer read(ByteBuffer buffer) {
        byte[] key = readSeq(buffer);
        byte[] val = readSeq(buffer);
        Store.INSTANCE.put(key, val);
        return ByteBuffer.wrap(new byte[]{0});
    }

    @Override
    public byte supportType() {
        return 1;
    }
}
