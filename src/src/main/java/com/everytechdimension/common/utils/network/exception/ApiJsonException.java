package com.everytechdimension.common.utils.network.exception;

import java.io.PrintWriter;

public class ApiJsonException extends ApiException {
    public final String bodyStr;

    public ApiJsonException(String msg, String body) {
        super(msg);
        this.bodyStr = body;
    }

    public ApiJsonException(String msg, String body, int code) {
        super(msg, code);
        this.bodyStr = body;
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        s.println("response: " + bodyStr);
        super.printStackTrace(s);
    }
}
