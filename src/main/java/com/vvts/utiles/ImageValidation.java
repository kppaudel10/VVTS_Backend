package com.vvts.utiles;

import javax.servlet.http.Part;
import java.io.IOException;

/**
 * @auther kul.paudel
 * @created at 2023-04-18
 */
public class ImageValidation {

    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png"};
    private static final int MAX_FILE_SIZE = 500; // 500 KB

    public static boolean validateImage(Part filePart) throws IOException {
        String fileName = filePart.getSubmittedFileName();
        String contentType = filePart.getContentType();
        long fileSize = filePart.getSize();

        // Check file extension
        String fileExtension = getFileExtension(fileName);
        boolean allowedExtension = false;
        for (String extension : ALLOWED_EXTENSIONS) {
            if (fileExtension.equalsIgnoreCase(extension)) {
                allowedExtension = true;
                break;
            }
        }

        // Check file size
        boolean allowedSize = fileSize <= MAX_FILE_SIZE;

        return allowedExtension && allowedSize;
    }

    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
