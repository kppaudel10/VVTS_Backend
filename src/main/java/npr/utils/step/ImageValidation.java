package npr.utils.step;

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

    /*
    --------------------------------INFO-----------------------------------
    This Class contain methods which are used to check the image extension
    is valid or not and size of selected images.
     */
    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png"};
    private static final int MAX_FILE_SIZE = 500 * 1000; // 500 KB

    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public String validateImage(MultipartFile filePart) throws IOException {
        // get actual file extension
        String fileName = filePart.getOriginalFilename();
        long fileSize = filePart.getSize();
        // Check file extension is available inside the allowed extension
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

}
