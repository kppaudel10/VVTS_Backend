package npr.step_fourth;

import npr.step_third.EdgeDetection;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

/**
 * @auther kul.paudel
 * @created at 2023-06-03
 */
public class AdaptiveThresholding {

    public Mat adaptiveThresholding(String inputImageFilePath) throws Exception {
        // Return type is set to Mat because we need to
        // pass the output image of this method for further processing.

        // Creating object called image, of EdgeDetection class
        EdgeDetection image = new EdgeDetection();

        // Creating a Mat object to store the output image from the previous class
        Mat detectingEdgesImage = image.detectingEdges(inputImageFilePath);

        // Creating new Mat object to store the output after blurring and edge detection.
        Mat featureExtractedImage = new Mat();

        Imgproc.adaptiveThreshold(detectingEdgesImage, featureExtractedImage, 255,
                Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 15);

        /*
         * AdaptiveThresholding -> Applies an adaptive threshold to an array. Threshold value is calculated
         * for smaller regions. Parameters :-
         * SourceImage,
         * DestinationImage,
         * MaxValue,
         * AdaptiveMethod -> Adaptive thresholding algorithm to be used
         * (ADAPTIVE_THRESh_MEAN_C -> The threshold value is the mean of the neighborhood area minus the constant C.),
         * ThresholdType -> BinaryThresholding technique used. (Explained while calculating CannyThresholdValue),
         * BlockSize -> size of pixel neighborhood used to calculate the threshold value. (similar to kernel size)
         * Constant -> required to calculate threshold. (mean of neighborhood area - constant C)
         */

        String storeThresholdImage = System.getProperty("user.home") + File.separator + "vvts/scan_image/threshold.png";
        Imgcodecs.imwrite(storeThresholdImage, featureExtractedImage);

        System.out.println("---------------------------------------------------");
        System.out.println("Adaptive Thresholding Filter is successfully applied\n");
        System.out.println("---------------------------------------------------");

        System.out.println("____________________________________________________");
        System.out.println("	ADAPTIVE THRESHOLDING PHASE COMPLETED");
        System.out.println("____________________________________________________\n");

        System.out.println("*******************************************************");

        System.out.println("____________________________________________________");
        System.out.println("	BEGINNING OF BOUNDING BOX PHASE");
        System.out.println("____________________________________________________\n");

        return featureExtractedImage;
    }

}
