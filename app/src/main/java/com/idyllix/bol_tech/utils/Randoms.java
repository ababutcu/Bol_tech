package com.idyllix.bol_tech.utils;

import java.util.Random;

public class Randoms {

    public static String createRandomString(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();
        StringBuilder builder = new StringBuilder(GetCurrentDate.getToday());
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            builder.append((char) randomLimitedInt);
        }
        return builder.toString().toUpperCase();
    }
}
