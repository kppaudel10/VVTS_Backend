package com.vvts.utiles;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

/**
 * @auther kul.paudel
 * @created at 2023-05-04
 */
@Component
public class ImageScanner {

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

        it.setDatapath("/usr/share/tesseract-ocr/5/tessdata");
        it.setLanguage("eng");

        // doing OCR on the image
        // and storing result in string str
        String str = it.doOCR(fopimage);
        System.out.println(str);
        str.replace("\n","");
        return str;
    }

    public String scan(String imageFilePath) throws TesseractException, IOException {
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
        return output.replace("\n","");
    }
}
