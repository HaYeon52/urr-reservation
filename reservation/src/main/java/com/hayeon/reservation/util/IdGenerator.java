package com.hayeon.reservation.util;

import java.util.concurrent.ThreadLocalRandom;

public final class IdGenerator {

    private static final String ALNUM = "abcdefghijklmnopqrstuvwxyz0123456789";

    private IdGenerator() {
    }

    /** DB 컬럼 길이 13에 맞춘 랜덤 ID */
    public static String randomId13() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(13);
        for (int i = 0; i < 13; i++) {
            sb.append(ALNUM.charAt(r.nextInt(ALNUM.length())));
        }
        return sb.toString();
    }
}
