package com.vvts.utiles;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * @auther kul.paudel
 * @created at 2023-04-18
 */
@Component
public class ImageValidation {

    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png"};
    private static final int MAX_FILE_SIZE = 500 * 1000; // 500 KB

    public static String validateImage(MultipartFile filePart) throws IOException {
        String fileName = filePart.getOriginalFilename();
        long fileSize = filePart.getSize();
        // Check file extension
        String fileExtension = getFileExtension(fileName);
        boolean allowedExtension = false;
        String extensionName = "";
        for (String extension : ALLOWED_EXTENSIONS) {
            if (fileExtension.equalsIgnoreCase(extension)) {
                allowedExtension = true;
                extensionName = extension;
                break;
            }
        }
        if (!allowedExtension) {
            throw new RemoteException("Invalid images extension");
        }

        // Check file size
        boolean allowedSize = fileSize <= MAX_FILE_SIZE;
        if (!allowedSize) {
            throw new RuntimeException("Images size must be maximum of " + 500 + " KB.");
        }
        return extensionName;
    }

    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
