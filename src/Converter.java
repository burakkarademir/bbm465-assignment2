package com.company;

import java.math.BigInteger;


public class Converter {
    public String ByteToHex(byte [] bytevalue){
        StringBuilder hex = new StringBuilder();
        for (byte b : bytevalue) {
            hex.append(String.format("%02X", b));
        }
        return hex.toString();


    }
    public  byte [] HexToByte(String hexvalue){
        byte[] b = new BigInteger(hexvalue,16).toByteArray();
        return b;

    }
}
