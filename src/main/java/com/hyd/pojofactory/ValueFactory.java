package com.hyd.pojofactory;

import java.security.SecureRandom;
import java.util.Random;

public class ValueFactory {

    static final Random RANDOM = new SecureRandom();

    static final char[] CHARS =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    public static String randomString(int minLength, int maxLength) {
        int length = RANDOM.nextInt(maxLength + 1 - minLength) + minLength;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHARS[RANDOM.nextInt(CHARS.length)]);
        }
        return sb.toString();
    }

    public static long randomLong(long min, long max) {
        long l = RANDOM.nextLong();
        return l >= min && l <= max ? l : (l % (max - min + 1)) + min;
    }

    // 0.0 - 1.0
    public static double randomDouble() {
        return RANDOM.nextDouble();
    }

    public static Object randomItem(Object[] items) {
        return items == null || items.length == 0 ? null :
                items[RANDOM.nextInt(items.length)];
    }
}
