package com.everytechdimension.common.utils.network;

public class Main {
    public static void main(String[] args) {
        Package pack = Main.class.getPackage();
        System.out.println(pack.getImplementationTitle() + ":" + pack.getImplementationVersion() + " by " + pack.getImplementationVendor() + "(" + pack.getName() + ")");
    }
}
