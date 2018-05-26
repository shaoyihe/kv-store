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
public class DelAction extends Action {
    @Override
    public ByteBuffer read(ByteBuffer buffer) {
        byte[] key = ByteBufferUtil.readSeq(buffer);
        byte[] val = Store.get().del(key);
        L.log.info("del " + str(key) + " and got " + str(val));
        ByteBuffer byteBuffer;
        if (val == null) {
            return ByteBufferUtil.allocateWith(DEL_NO_VAL);
        } else {
            return ByteBufferUtil.allocateWithBytes(DEL_SUCCESS, val);
        }
    }

    @Override
    public byte supportType() {
        return DEL_ACTION;
    }
}
