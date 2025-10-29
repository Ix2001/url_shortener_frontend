package com.alabuga.shortener.util;

public class Base62 {
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALPHABET.length();

    public static String encode(long value) {
        if (value == 0) return "0";
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(ALPHABET.charAt((int)(value % BASE)));
            value /= BASE;
        }
        return sb.reverse().toString();
    }

    public static long decode(String str) {
        long num = 0;
        for (char c : str.toCharArray()) {
            int i = ALPHABET.indexOf(c);
            if (i < 0) throw new IllegalArgumentException("Invalid Base62 char: " + c);
            num = num * BASE + i;
        }
        return num;
    }
}