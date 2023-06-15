package npr.utils.step.fifth;

import marvin.image.MarvinImage;
import marvin.image.MarvinSegment;
import marvin.io.MarvinImageIO;
import npr.utils.step.fourth.AdaptiveThresholding;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.*;
import java.util.List;

import static marvin.MarvinPluginCollection.findTextRegions;

/**
 * @auther kul.paudel
 * @created at 2023-06-03
 */
public class BoundedBox {

    public BoundedBox(String inputImageFilePath) throws Exception {
        AdaptiveThresholding image = new AdaptiveThresholding(); // Creating object called image, of AdaptiveThresholding class
        Mat thresholded_image = image.adaptiveThresholding(inputImageFilePath); // Creating a Mat object to store the output image from the the previous class


        String image_location_for_marvin = "/home/kul-java/vvts/scan_image/Output1.png";
        // location for storing the output from previous class

        String output_after_marvin = "/home/kul-java/vvts/scan_image/boxed.png";
        // location for storing the output of this class

        Imgcodecs.imwrite(image_location_for_marvin, thresholded_image); // Saving the output of previous class in LocalFileSystem

        MarvinImage bounded_box_image = MarvinImageIO.loadImage(image_location_for_marvin); // reading the image from LocalFileSystem as
        // MarvinImage. (This is the image that was the output of previous class that was written to LocalFileSystem)

        try {
            bounded_box_image = findText(bounded_box_image, 30, 20, 100, 170, output_after_marvin); // Storing the result of findText function in bounded_box_image
        } catch (Exception e) {
            System.out.println(e);
        }
        MarvinImageIO.saveImage(bounded_box_image, output_after_marvin); // Saving the output of this class to LocalFileSystem

    }

    public static MarvinImage findText(MarvinImage bounded_box_image, int maxWhiteSpace, int maxFontLineWidth, int minTextWidth, int grayScaleThreshold, String output_after_marvin) {
        List<MarvinSegment> segments = findTextRegions(bounded_box_image, maxWhiteSpace, maxFontLineWidth, minTextWidth, grayScaleThreshold);

        /*
         * findTextRegions -> an inbuilt method used to draw bounding boxes around characters in an image.
         *
         * List<MarvinSegment> Finds instances of a given image in another image with perfect matching (exactly the same pixels value).
         * Parameters are as follows:- (Optimal Values are selected after a lot of testing/trials -> 30, 20, 100, 170 respectively)
         * SourceImage,
         * maxWhiteSpace -> max white pattern width,
         * maxFontLineWidth -> max black pattern width,
         * minTextWidth -> min text width,
         * grayScaleThreshold -> grayscale image threshold for finding pattern
         */

        for (MarvinSegment s : segments) { // for each loop
            if (s.height >= 10) {
                s.y1 -= 20; // This entire function is used to draw a rectangle.
                s.y2 += 20;
                bounded_box_image.drawRect(s.x1, s.y1, s.x2 - s.x1, s.y2 - s.y1, Color.red);
                bounded_box_image.drawRect(s.x1 + 1, s.y1 + 1, (s.x2 - s.x1) - 2, (s.y2 - s.y1) - 2, Color.red);
                bounded_box_image.drawRect(s.x1 + 2, s.y1 + 2, (s.x2 - s.x1) - 4, (s.y2 - s.y1) - 4, Color.red);

                /*
                 * drawRect -> draws the outline of a rectangle
                 * x1 -> x coordinate of rectangle that is to be drawn,
                 * y1 -> y coordinate of rectangle that is to be drawn,
                 * x2 -> width of rectangle to be drawn,
                 * y2 -> height of rectangle to be drawn
                 */
            }
        }

        System.out.println("---------------------------------------------------");
        System.out.println("A bounded box has been created around the license plate");
        System.out.println("Output is stored at : " + output_after_marvin);
        System.out.println("---------------------------------------------------\n");

        System.out.println("____________________________________________________");
        System.out.println("	BOUNDING BOX PHASE COMPLETED");
        System.out.println("____________________________________________________\n");

        System.out.println("******************************************************************************************************************************");

        return bounded_box_image;
    }


}
