package com.vvts.utiles;

import java.util.UUID;

/**
 * @auther kul.paudel
 * @created at 2023-04-18
 */
public class ImageUtils {

    public static String generateRandomImageName(String name, String mobileNumber, String imageName) {
        // Generate a random UUID
        String uuid = UUID.randomUUID().toString();

        // Remove dashes from UUID
        uuid = uuid.replaceAll("-", "");

        // Concatenate the UUID with the given information
        String randomName = name + "_" + mobileNumber + "_" + imageName + "_" + uuid;

        // Remove any non-alphanumeric characters and convert to lowercase
        randomName = randomName.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        return randomName;
    }

}
