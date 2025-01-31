package com.common.util;

import java.io.UnsupportedEncodingException;

public class ByteUtils {
    public static byte[] byteMerger(byte[] bt1, byte[] bt2) {
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    public static byte[] byteMerger(byte[] bt1, String str) {
        try {
            return byteMerger(bt1, str.getBytes("utf-8"));
        } catch(UnsupportedEncodingException ex) {
            return null;
        }
    }
}
