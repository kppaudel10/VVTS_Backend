package com.vvts.utiles;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @auther kul.paudel
 * @created at 2023-04-18
 */
@Component
public class ImageUtils {

    public String generateUniqueImageName(String name, Integer userId, String imageName, String extension) {
        // Generate a random UUID
       String updateName = name.replace(" ", "_");
        String uniqueName = "";
        uniqueName = updateName.concat("_".concat(String.valueOf(userId))).concat("_".concat(imageName)).concat(".".concat(extension));
        return uniqueName;
    }

    public String generateRandomString() {
        int length = 5;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public int generateRandomInt() {
        Random random = new Random();
        return random.nextInt(9000) + 1000;
    }

}
