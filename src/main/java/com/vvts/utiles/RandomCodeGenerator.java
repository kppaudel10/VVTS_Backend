package com.vvts.utiles;

import java.util.Random;

/**
 * @auther kul.paudel
 * @created at 2023-05-01
 */
public class RandomCodeGenerator {

    public String generateRandomCode(int length) {
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // Possible characters in the code
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphanumeric.length());
            codeBuilder.append(alphanumeric.charAt(index));
        }
        return codeBuilder.toString();
    }

}
