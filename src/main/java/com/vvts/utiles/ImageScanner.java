package com.vvts.utiles;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @auther kul.paudel
 * @created at 2023-05-04
 */
@Component
public class ImageScanner {

    public static File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public String processImg(BufferedImage ipimage,
                             float scaleFactor, float offset) throws IOException, TesseractException {
        // Making an empty image buffer
        // to store image later
        // ipimage is an image buffer
        // of input image
        BufferedImage opimage
                = new BufferedImage(1050,
                1024,
                ipimage.getType());

        // creating a 2D platform
        // on the buffer image
        // for drawing the new image
        Graphics2D graphic
                = opimage.createGraphics();

        // drawing new image starting from 0 0
        // of size 1050 x 1024 (zoomed images)
        // null is the ImageObserver class object
        graphic.drawImage(ipimage, 0, 0,
                1050, 1024, null);
        graphic.dispose();

        // rescale OP object
        // for gray scaling images
        RescaleOp rescale
                = new RescaleOp(scaleFactor, offset, null);

        // performing scaling
        // and writing on a .png file
        BufferedImage fopimage
                = rescale.filter(opimage, null);
        ImageIO.write(fopimage,
                "jpg",
                new File("/home/kul-java/vvts/output.png"));

        // Instantiating the Tesseract class
        // which is used to perform OCR
        Tesseract it = new Tesseract();

        it.setDatapath(getFolderAbsolutePath("tessdata"));
        it.setLanguage("eng");

        // doing OCR on the image
        // and storing result in string str
        String str = it.doOCR(fopimage);
        System.out.println("scan output:.............................: " + str);
        str.replace("\n", "");
        return str;
    }

    public String scan(String imageFilePath, String LanguageCode) throws TesseractException, IOException {
        File f = new File(imageFilePath);

        BufferedImage ipimage = ImageIO.read(f);

        // getting RGB content of the whole image file
        double d
                = ipimage
                .getRGB(ipimage.getTileWidth() / 2,
                        ipimage.getTileHeight() / 2);

        String output = null;
        // comparing the values
        // and setting new scaling values
        // that are later on used by RescaleOP
        if (d >= -1.4211511E7 && d < -7254228) {
            output = processImg(ipimage, 3f, -10f);
        } else if (d >= -7254228 && d < -2171170) {
            output = processImg(ipimage, 1.455f, -47f);
        } else if (d >= -2171170 && d < -1907998) {
            output = processImg(ipimage, 1.35f, -10f);
        } else if (d >= -1907998 && d < -257) {
            output = processImg(ipimage, 1.19f, 0.5f);
        } else if (d >= -257 && d < -1) {
            output = processImg(ipimage, 1f, 0.5f);
        } else if (d >= -1 && d < 2) {
            output = processImg(ipimage, 1f, 0.35f);
        }
        return output.replace("\n", "");
    }

    public String doOCR(MultipartFile multipartFile, String LanguageCode) {
        ITesseract instance = new Tesseract();

        try {

            BufferedImage in = ImageIO.read(convert(multipartFile));

            BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = newImage.createGraphics();
            g.drawImage(in, 0, 0, null);
            g.dispose();

            instance.setLanguage(LanguageCode);
//            instance.setDatapath("/home/kul-java/Desktop/6TH_SEM/Project-II/VVTS_Backend/tessdata");
            instance.setDatapath(getFolderAbsolutePath("tessdata"));
            String result = instance.doOCR(newImage);
            System.out.println(result);

            return result;

        } catch (TesseractException | IOException e) {
            System.err.println(e.getMessage());
            return "Error while reading image";
        }

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

    public static void main(String[] args) {
//        System.loadLibrary();
    }
}
