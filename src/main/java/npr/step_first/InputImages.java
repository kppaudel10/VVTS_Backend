package npr.step_first;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @auther kul.paudel
 * @created at 2023-06-03
 */
public class InputImages {

    public Mat downSampling(String inputImageFilePath) throws Exception {
        // pass the output image of this method for further processing.
        System.out.println("____________________________________________________");
        System.out.println("	BEGINNING OF DOWNSAMPLING PHASE");
        System.out.println("____________________________________________________\n");

        // Load OpenCV library
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // this helps to load
        nu.pattern.OpenCV.loadLocally();
        // the OpenCV libraries.

//        Mat src = Imgcodecs.imread("/home/kul-java/Pictures/carThree.jpeg"); // Reading the image and storing in
        Mat src = Imgcodecs.imread(inputImageFilePath); // Reading the image and storing in
        // a matrix object (Mat src)

        System.out.println("----------------------------------------------------");
        System.out.println("The size of the original image = " + src.size());

        double widthOriginal = src.size().width; // width
        double heightOriginal = src.size().height; // height

        double downScalingWidthFactor = 3;    // d_h_f must be less than d_w_f
        double downScalingHeightFactor = 2;

        /*
         * Value of downscaling_width_factor is larger due to 2 reasons :-
         * 1) We can compress more image data in the horizontal direction
         * because the width of a license plate is greater than height.
         * 2) Large d_w_f also makes the characters of license plate more
         * compact which help in later steps (feature extraction).
         */

        double finalWidth = widthOriginal / downScalingWidthFactor;
        double finalHeight = heightOriginal / downScalingHeightFactor;
        // final widths and heights for resizing.

        System.out.println("----------------------------------------------------");
        System.out.println("Original Width = " + widthOriginal + "\nOriginal Height = " + heightOriginal);
        System.out.println("----------------------------------------------------");
        System.out.println("Final Width = " + finalWidth + "\nFinal Height = " + finalHeight);
        System.out.println("----------------------------------------------------");


        Mat resizeImage = new Mat();
        // New Mat object to store the downsampled image
        Size scaleSize = new Size(finalWidth, finalHeight);
        // Defining the size for the down sampled image in Size Object->(scaleSize)

        Imgproc.resize(src, resizeImage, scaleSize, 0, 0, Imgproc.INTER_AREA);
        /*
         * Applying the resize function with the following parameters :-
         * SourceImage,
         * DestinationImage,
         * SizeRequired (width, height),
         * ScalingFactor(dx, dy),
         * InterpolationMethod
         *
         * We have used INTER_AREA because it works best when we need to shrink an image.
         * INTER_AREA is BilinearInterpolation with coefficients 1 and 0
         *
         * TO BE FURTHER EXPLAINED
         *
         */

        System.out.println("The size of the final image = " + resizeImage.size());
        System.out.println("---------------------------------------------------");

        System.out.println("\n____________________________________________________");
        System.out.println("	  DOWNSAMPLING PHASE COMPLETED");
        System.out.println("____________________________________________________\n");

        System.out.println("******************************************************************************************************************************\n");


        System.out.println("____________________________________________________");
        System.out.println("	BEGINNING OF GRAYSCALE CONVERSION PHASE");
        System.out.println("____________________________________________________\n");
        String storeDownScaledImage = "/home/kul-java/vvts/scan_image/output.png";
        Imgcodecs.imwrite(storeDownScaledImage, resizeImage);

        return resizeImage; // Returning the resize image so that further

    }
}
