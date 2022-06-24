package com.everytechdimension.common.utils.network.exception;

public class ApiException extends Exception {
    public final int code;

    public ApiException(String msg) {
        this(msg, 501);
    }

    public ApiException(String msg, int code) {
        super(msg);
        this.code = code;
    }
}
