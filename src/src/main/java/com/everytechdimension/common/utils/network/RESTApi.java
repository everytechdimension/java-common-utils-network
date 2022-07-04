package com.everytechdimension.common.utils.network;

import com.everytechdimension.common.utils.network.exception.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RESTApi {
    public static JSONObject httpRequestInJSON(
            REQUEST_METHOD method,
            String uri,
            HashMap<String, String> headers
    ) throws ApiException {
        return httpRequestInJSON(method, uri, headers, null);
    }

    public static JSONObject httpRequestInJSON(
            REQUEST_METHOD method,
            String uri
    ) throws ApiException {
        return httpRequestInJSON(method, uri, new HashMap<String, String>());
    }

    public static JSONObject httpRequestInJSON(
            String uri
    ) throws ApiException {
        return httpRequestInJSON(REQUEST_METHOD.GET, uri, new HashMap<String, String>());
    }

    public static JSONObject httpRequestInJSON(
            REQUEST_METHOD method,
            String uri,
            HashMap<String, String> headers,
            String reqBodyStr
    ) throws ApiException {
        HTTPResponse res = httpRequest(method, uri, headers, reqBodyStr);
        try {
            JSONObject json = new JSONObject(res.response);
            if (res.statusCode == 200)
                return json;

            throw new ApiResponseException("Error is thrown from Api Server", res.statusCode);
        } catch (JSONException e) {
            throw new ApiJsonException("Api response is not json: " + e.getMessage(), res.response+"\n uri: "+ uri);
        }
    }

    public static HTTPResponse httpRequest(
            REQUEST_METHOD method,
            String uri,
            HashMap<String, String> headers,
            String reqBodyStr
    ) throws ApiException {
        HttpURLConnection connection = null;
        Scanner scanner = null;
        String response = "";
        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(120000);

            if (reqBodyStr != null) {
                connection.setDoOutput(true);
                connection.setDoInput(true);
            }
            connection.setRequestMethod(method.name());
            for (Map.Entry<String, String> p : headers.entrySet())
                connection.setRequestProperty(p.getKey(), p.getValue());

            if (reqBodyStr != null) {
                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(reqBodyStr);
                osw.flush();
                osw.close();
            }
            int statusCode = connection.getResponseCode();
            boolean success = 200 <= statusCode && 299 >= statusCode;
            final InputStream inputStream = success ? connection.getInputStream() : connection.getErrorStream();
            scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (!hasInput)
                throw new ApiException("Api response is empty");

            response = scanner.next();
            return new HTTPResponse(statusCode, response);
        } catch (MalformedURLException e) {
            throw new ApiException("Uri: " + uri + ", could not be parsed");
        } catch (SSLHandshakeException e) {
            throw new ApiException("The Server could not be reached. There is an issue with secure socket communication.");
        } catch (UnknownHostException e) {
            throw new ApiException("The Server address could not be resolved. Please make sure your Network is working correctly.");
        } catch (IOException e) {
            if (isInternetAccessible())
                throw new ApiException("The Server couldn't be reached");
            else
                throw new ApiException("Sorry, Please check your internet connection.");
        } finally {
            if (scanner != null)
                scanner.close();

            if (connection != null)
                connection.disconnect();
        }
    }

    private static boolean isInternetAccessible() {
        HttpURLConnection con = null;
        try {
            URL obj = new URL("https://www.google.com");
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", "Test");
            con.setRequestProperty("Connection", "close");
            con.setConnectTimeout(1500);
            con.setRequestMethod("HEAD");
            con.connect();
            return con.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception e) {
                }
            }
        }
    }

    public static void main(String[] args) {
    }

    public enum REQUEST_METHOD {GET, POST, PUT, HEAD}
}