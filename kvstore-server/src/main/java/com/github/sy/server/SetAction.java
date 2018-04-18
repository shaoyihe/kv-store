package com.github.sy.server;

import com.github.sy.common.ByteBufferUtil;

import java.nio.ByteBuffer;

/**
 * on 2018/4/17.
 */
public class SetAction extends Action {
    @Override
    public ByteBuffer read(ByteBuffer buffer) {
        byte[] key = ByteBufferUtil.readSeq(buffer);
        byte[] val = ByteBufferUtil.readSeq(buffer);
        L.log.info("got set " + str(key) + " " + str(val));
        Store.INSTANCE.put(key, val);
        return ByteBuffer.wrap(new byte[]{0});
    }

    @Override
    public byte supportType() {
        return SET_ACTION;
    }
}
