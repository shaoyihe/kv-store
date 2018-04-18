package com.github.sy.server;

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
        byte[] key = readSeq(buffer);
        byte[] val = Store.INSTANCE.get(key);
        L.log.info("got get " + str(key) + " " + str(val));
        ByteBuffer byteBuffer;
        if (val == null) {
            byteBuffer = ByteBuffer.allocate(1);
            byteBuffer.put((byte) 0);
        } else {
            byteBuffer = ByteBuffer.allocate(1 + 4 + val.length);
            byteBuffer.put((byte) 1);
            byteBuffer.putInt(val.length);
            byteBuffer.put(val);
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    @Override
    public byte supportType() {
        return 0;
    }
}
