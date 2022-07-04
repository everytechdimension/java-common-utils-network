package com.everytechdimension.common.utils.network.exception;

public class ApiResponseException extends ApiException {
    public ApiResponseException(String msg) {
        super(msg);
    }

    public ApiResponseException(String msg, int code) {
        super(msg, code);
    }
}
