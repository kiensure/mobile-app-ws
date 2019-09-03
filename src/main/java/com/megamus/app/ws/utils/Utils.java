/**
 * 
 */
package com.megamus.app.ws.utils;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

/**
 * @author mrens
 *
 */
@Component
public class Utils {

    private final Random RANDOM = new SecureRandom();
    private final String ANPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Generate public Id for user
     * 
     * @param length Number of
     * @return public UserId
     */
    public String generateUserId(int length) {
        return generateRandomString(length);
    }

    public String generateAddressId(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder randomValue = new StringBuilder();

        for (int i = 0; i < length; i++) {
            randomValue.append(ANPHABET.charAt(RANDOM.nextInt(ANPHABET.length())));
        }

        return new String(randomValue);
    }
}
