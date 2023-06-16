package npr.step_third;

import npr.step_second.GrayScale;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @auther kul.paudel
 * @created at 2023-06-03
 */
public class EdgeDetection {

    public Mat detectingEdges(String inputImageFilePath) throws Exception { // Return type is set to Mat because we need to
        // pass the output image of this method for further processing.


        GrayScale image = new GrayScale(); // Creating object called image, of GrayScale class
        Mat grayscale_image = image.grayscaleConversion(inputImageFilePath); // Creating a Mat object to store the output image from the the previous class

        Mat detected_edges_image = new Mat(); // Creating new Mat object to store the output after blurring and edge detection.

        String store_gray_image = "/home/kul-java/vvts/scan_image/gray.png";
        Imgcodecs.imwrite(store_gray_image, grayscale_image);
        Imgproc.blur(grayscale_image, grayscale_image, new Size(3.7, 3.7), new Point(-1, -1));

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
        MatOfDouble standard_deviation = new MatOfDouble(); // MOD object for standard_deviation
        Core.meanStdDev(grayscale_image, mean, standard_deviation);
        // meanStdDev -> calculates the mean and the standard deviation of array elements

        double median = mean.get(0, 0)[0];
        //mean.get(0, 0) returns a double[], therefore, to get the first element we simply add [0].

        System.out.println("---------------------------------------------------");
        System.out.println("Median Value = " + median);

        double CannyThresholdValue = Imgproc.threshold(grayscale_image, detected_edges_image, 120, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
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

        System.out.println("---------------------------------------------------");
        System.out.println("Canny Threshhold Factor = " + CannyThresholdValue);
        System.out.println("---------------------------------------------------\n");

        Imgproc.Canny(grayscale_image, grayscale_image, CannyThresholdValue, CannyThresholdValue * 2, 3, false);
        /*
         * The function finds edges in the input image and marks them in the output map edges using the Canny algorithm.
         * The LowerThresholdValue is used for edge linking.
         * The UpperThresholdValue is used to find initial segments of strong edges. Parameters :-
         * SourceImage,
         * DestinationImage,
         * LowerThresholdValue -> CannyThresholdValue,
         * UpperThresholdValue -> 2 times of CannyThresholdValue,
         * ApertureSize/KernelSize for the Sobel operator. (Sobel operator is used to create an image that puts emphasis on edges).
         * It can be [3, 5, 7]. LowerValue results in less details and therefore less noise,
         * L2Gradient -> a flag, indicating whether a more detailed norm should
         * be used to calculate the result or the standard norm. (L2 = true -> more detailed norm, L2 = false -> standard norm)
         */

        Core.add(grayscale_image, Scalar.all(0), detected_edges_image);
        // add() -> Calculate the per-element sum of a matrix and Scalar and stores it in a Mat object,
        //.all(0) -> setting the scalar value = 0 across all channels,
        // Basically overlays the scalar values over a mat object.
        // Scalar is mostly used to alter the background color of an output mat object.


        String store_edge_detected_image = "/home/kul-java/vvts/scan_image/edge.png";
        Imgcodecs.imwrite(store_edge_detected_image, detected_edges_image);


        System.out.println("____________________________________________________");
        System.out.println("	EDGE DETECTION PHASE COMPLETED");
        System.out.println("____________________________________________________\n");

        System.out.println("******************************************************************************************************************************");

        System.out.println("____________________________________________________");
        System.out.println("	BEGINNING OF ADAPTIVE THRESHOLDING PHASE");
        System.out.println("____________________________________________________\n");

        return detected_edges_image;
    }
}
