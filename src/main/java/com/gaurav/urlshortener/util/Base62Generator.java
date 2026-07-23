package com.gaurav.urlshortener.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Base62Generator {

    private static final String BASE62 =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final int SHORT_CODE_LENGTH = 7;

    private static final Random RANDOM = new Random();

    public String generateShortCode() {

        StringBuilder shortCode = new StringBuilder(SHORT_CODE_LENGTH);

        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            int index = RANDOM.nextInt(BASE62.length());
            shortCode.append(BASE62.charAt(index));
        }

        return shortCode.toString();
    }
}