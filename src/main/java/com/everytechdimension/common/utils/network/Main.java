package com.everytechdimension.common.utils.network;

import com.everytechdimension.common.utils.network.exception.ApiException;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        Package pack = Main.class.getPackage();
        System.out.println(pack.getImplementationTitle() + ":" + pack.getImplementationVersion() + " by " + pack.getImplementationVendor() + "(" + pack.getName() + ")");
        try {
            JSONObject json = RESTApi.httpRequestInJSON(RESTApi.REQUEST_METHOD.GET, "https://fadbrandz.com/test.json");
            System.out.println("------------------------------------");
            System.out.println(json.toString());
            System.out.println("------------------------------------");
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
