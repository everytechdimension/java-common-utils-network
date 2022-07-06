package com.everytechdimension.common.utils.network;

public class HTTPResponse {
    public final String response;
    public final int statusCode;

    public HTTPResponse(int statusCode, String response){
        this.response = response;
        this.statusCode = statusCode;
    }
}
