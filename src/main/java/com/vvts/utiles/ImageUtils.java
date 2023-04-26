package com.vvts.utiles;

import org.springframework.stereotype.Component;

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


}
