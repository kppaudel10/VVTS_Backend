package npr.step_third;

import npr.step_second.GrayScale;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

/**
 * @auther kul.paudel
 * @created at 2023-06-03
 */
public class EdgeDetection {

    public Mat detectingEdges(String inputImageFilePath) throws Exception {
        // Return type is set to Mat because we need to
        // pass the output image of this method for further processing.

        // Creating object called image, of GrayScale class
        GrayScale image = new GrayScale();
        // Creating a Mat object to store the output image from  the previous class
        Mat grayScaleImage = image.grayscaleConversion(inputImageFilePath);

        // Creating new Mat object to store the output after blurring and edge detection.
        Mat detectedEdgesImage = new Mat();

        String storeGrayImage = System.getProperty("user.home") + File.separator + "vvts/scan_image/gray.png";
        Imgcodecs.imwrite(storeGrayImage, grayScaleImage);
        Imgproc.blur(grayScaleImage, grayScaleImage, new Size(3.7, 3.7), new Point(-1, -1));

        /*
         * Blurring is a simple and frequently used image processing operation
         * in order to reduce noise. Parameters used are :-
         * SourceImage,
         * DestinationImage,
         * SizeOfKernel (Kernel is like a mask/filter that tells us how to change values of any given pixel by
         * combining it with different amounts of neighboring pixels.),
         * AnchorPoint (Location of anchor point w.r.t neighborhood. Negative value of anchor point means that
         * center of the kernel is the anchor point. )
         */

//		HighGui.imshow("Blurred Image", grayscale_image);
//		HighGui.waitKey();
//
        MatOfDouble mean = new MatOfDouble(); // The MatOfDouble is same as Mat
        // except it allows values that are not necessarily integers.
        // MOD object for mean
        MatOfDouble standardDeviation = new MatOfDouble(); // MOD object for standard_deviation
        Core.meanStdDev(grayScaleImage, mean, standardDeviation);
        // meanStdDev -> calculates the mean and the standard deviation of array elements

        double median = mean.get(0, 0)[0];
        //mean.get(0, 0) returns a double[], therefore, to get the first element we simply add [0].


        double CannyThresholdValue = Imgproc.threshold(grayScaleImage, detectedEdgesImage, 120,
                255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        /*
         * Applies a fixed-level threshold to each array element.
         * The function is typically used to get a binary image out of
         * a grayscale image or for removing a noise, that is, filtering out pixels with too small or too large values. Parameter :-
         * SourceImage,
         * DestinationImage,
         * ThresholdValue,
         * MaximumValue -> Maximum Intensity Value,
         * ThresholdingTypes -> Using the Otsu Method and Binary Thresholding,
         *
         * Otsu Method -> returns a single intensity threshold that separate pixels into foreground and background.
         * Binary Thresholding -> If pixel intensity is greater than the set threshold (that is 120), value set to 255, else set to 0 (black).
         */

        Imgproc.Canny(grayScaleImage, grayScaleImage, CannyThresholdValue, CannyThresholdValue * 2, 3, false);

        Core.add(grayScaleImage, Scalar.all(0), detectedEdgesImage);

        String storeEdgeDetectedImage = System.getProperty("user.home") + File.separator + "vvts/scan_image/edge.png";
        Imgcodecs.imwrite(storeEdgeDetectedImage, detectedEdgesImage);

        return detectedEdgesImage;
    }
}
