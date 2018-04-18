package com.github.sy.server;

import java.nio.ByteBuffer;

/**
 * on 2018/4/17.
 */
public abstract class Action {

    public abstract ByteBuffer read(ByteBuffer buffer);

    byte[] readSeq(ByteBuffer buffer) {
        int keySize = buffer.getInt();
        byte[] val = new byte[keySize];
        buffer.get(val);
        return val;
    }

    public String str(byte[] bytes) {
        return bytes == null ? null : new String(bytes);
    }

    public abstract byte supportType();

    public static ByteBuffer processRequest(ByteBuffer buffer) {
        byte type = buffer.get();
        Action action = new GetAction();
        if (type == 1) {
            action = new SetAction();
        }
//        buffer.position(position);
        ByteBuffer response = action.read(buffer);
        buffer.flip();
        return response;
    }


}
