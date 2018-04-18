package com.github.sy.common;

/**
 * on 2018/4/18.
 */
public interface ResponseType {
    int RESPONSE_TYPE_IN_BYTES = 1;
    int DATA_SIZE_IN_BYTES = 4;

    byte GET_SUCCESS = 0;
    byte GET_NO_VAL = 1;

    byte DEL_SUCCESS = 0;
    byte DEL_NO_VAL = 1;

    byte SET_SUCCESS = 0;
    byte SET_WITH_NO_VAL = 1;

    //opr type
    byte GET_ACTION = 0;
    byte SET_ACTION = 1;
    byte DEL_ACTION = 2;
}
