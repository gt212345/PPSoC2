package com.hrw.ppsoc2.Interface;

/**
 * Created by PPSoC on 2015/4/23.
 */
public class ByteParse {
    static public float getFloatValue(Byte L,Byte H){
        float tempint;
        String temp = getBitstoString(H);
        temp += getBitstoString(L);
        tempint = Integer.parseInt(temp.substring(0,15) , 2);
        return tempint;
    }

    static private String getBitstoString(byte b){
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }
}
