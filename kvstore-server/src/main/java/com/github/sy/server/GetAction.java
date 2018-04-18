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
        ByteBuffer byteBuffer;
        if (val == null) {
            byteBuffer = ByteBuffer.allocate(RESPONSE_TYPE_IN_BYTES);
            byteBuffer.put(GET_NO_VAL);
        } else {
            byteBuffer = ByteBuffer.allocate(RESPONSE_TYPE_IN_BYTES + DATA_SIZE_IN_BYTES + val.length);
            byteBuffer.put(GET_SUCCESS);
            ByteBufferUtil.putSeq(byteBuffer, val);
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    @Override
    public byte supportType() {
        return GET_ACTION;
    }
}
