package com.github.sy.server;

import com.github.sy.common.ResponseType;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * on 2018/4/17.
 */
public abstract class Action implements ResponseType {

    public abstract ByteBuffer read(ByteBuffer buffer);

    public String str(byte[] bytes) {
        return bytes == null ? null : new String(bytes);
    }

    public abstract byte supportType();

    public static ByteBuffer processRequest(ByteBuffer buffer) {
        byte type = buffer.get();
        Action action = ACTION_MAP.get(type);
        ByteBuffer response = action.read(buffer);
        buffer.flip();
        return response;
    }

    private static Map<Byte, Action> ACTION_MAP = new HashMap<>();

    static{
        put(new GetAction(), new SetAction(), new DelAction());
    }

    private static void put(Action... actions) {
        for (Action action : actions) {
            ACTION_MAP.put(action.supportType(), action);
        }
    }

}
