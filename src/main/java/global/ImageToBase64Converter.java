package global;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @auther kul.paudel
 * @created at 2023-05-19
 */
@Component
public class ImageToBase64Converter {

    public static String convertImageToBase64(String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        FileInputStream fileInputStream = new FileInputStream(imageFile);
        byte[] imageData = new byte[(int) imageFile.length()];
        fileInputStream.read(imageData);

        return Base64.getEncoder().encodeToString(imageData);
    }

}
