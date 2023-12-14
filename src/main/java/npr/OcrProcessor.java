package npr;

import net.sourceforge.tess4j.Tesseract1;
import npr.step_fifth.BoundedBox;

import java.io.File;

/**
 * @auther kul.paudel
 * @created at 2023-06-03
 */
public class OcrProcessor {

    public String doOcr(String filePathName) throws Exception {

        BoundedBox boundedBox = new BoundedBox(filePathName);

        Tesseract1 tesseract = new Tesseract1();

        tesseract.setLanguage("eng");
        tesseract.setDatapath(getFolderAbsolutePath("tessdata"));
        // Set the path for Tesseract
        // so that it can use the training data that is created.

        String output = tesseract.doOCR(new File(filePathName));

        // Removing any whitespaces from the extracted output
        output = output.replaceAll("\\s", "");

        return output;
    }

    private String getFolderAbsolutePath(String folderName) {
        // Replace with the name of the folder you want to find the path for
        File folder = new File(folderName);

        if (folder.exists() && folder.isDirectory()) {
            String path = folder.getAbsolutePath();
            System.out.println("Path of the folder '" + folderName + "': " + path);
            return path;
        } else {
            System.out.println("Folder '" + folderName + "' does not exist.");
            return null;
        }
    }
}
