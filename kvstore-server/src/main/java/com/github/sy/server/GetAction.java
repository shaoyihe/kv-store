package com.github.sy.server;

import com.github.sy.common.ByteBufferUtil;

import java.nio.ByteBuffer;

/**
 * requst
 * <pre>
 *  请求
 *      位置
 *  请求类型|key占用字节|key
 *      字节占用
 *  1|4|~
 *  返回
 *
 *  </pre>
 * on 2018/4/17.
 */
public class GetAction extends Action {
    @Override
    public ByteBuffer read(ByteBuffer buffer) {
        byte[] key = ByteBufferUtil.readSeq(buffer);
        byte[] val = Store.INSTANCE.get(key);
        L.log.info("got get " + str(key) + " " + str(val));
        if (val == null) {
            return ByteBufferUtil.allocateWithBytes(GET_NO_VAL);
        } else {
            return ByteBufferUtil.allocateWithBytes(GET_SUCCESS, val);
        }
    }

    @Override
    public byte supportType() {
        return GET_ACTION;
    }
}
