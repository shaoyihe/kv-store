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
        byte[] preVal = Store.INSTANCE.put(key, val);
        if (preVal == null) {
            return ByteBufferUtil.allocateWith(SET_WITH_NO_VAL);
        }
        return ByteBufferUtil.allocateWithBytes(SET_SUCCESS, preVal);
    }

    @Override
    public byte supportType() {
        return SET_ACTION;
    }
}
