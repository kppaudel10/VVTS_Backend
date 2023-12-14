package npr.step_second;

import npr.step_first.InputImages;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * @auther kul.paudel
 * @created at 2023-06-03
 */
public class GrayScale {

    public Mat grayscaleConversion(String inputImageFilePath) throws Exception {

        // Creating object called image, of InputImages class
        InputImages image = new InputImages();
        Mat resizedImage = image.downSampling(inputImageFilePath);

        Mat grayScaleImage = new Mat();

        Imgproc.cvtColor(resizedImage, grayScaleImage, Imgproc.COLOR_RGB2GRAY);

        /*
         * CV_8UC3 -> an 8 bit unsigned integer matrix with 3 channels. Suggesting
         * that it is RGB (or BGR) image.
         *
         * CV_8UC1 -> an 8 bit unsigned integer matrix with a single channel.
         * Suggesting that it is grayscale image.
         *
         * Signed Integer -> Stored using 2's complement. Contains both positive and negative values (-1 to -128) and (0 to 127)
         *
         * Unsigned Integer -> It can hold a large positive value and no negative values. Ideal for intensity value that range
         * from 0 to 255.
         */

        return grayScaleImage;
    }
}
