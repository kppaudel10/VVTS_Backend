package npr.step_fifth;

import marvin.image.MarvinImage;
import marvin.image.MarvinSegment;
import marvin.io.MarvinImageIO;
import npr.step_fourth.AdaptiveThresholding;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.*;
import java.io.File;
import java.util.List;

import static marvin.MarvinPluginCollection.findTextRegions;

/**
 * @auther kul.paudel
 * @created at 2023-06-03
 */
public class BoundedBox {

    public BoundedBox(String inputImageFilePath) throws Exception {
        // Creating object called image, of AdaptiveThresholding class
        AdaptiveThresholding image = new AdaptiveThresholding();

        // Creating a Mat object to store the output image from the previous class
        Mat thresholdImage = image.adaptiveThresholding(inputImageFilePath);

        String imageLocationForMarvin = System.getProperty("user.home") + File.separator + "vvts/scan_image/Output1.png";
        // location for storing the output from previous class

        String outputAfterMarvin = System.getProperty("user.home") + File.separator + "vvts/scan_image/boxed.png";
        // location for storing the output of this class

        // Saving the output of previous class in LocalFileSystem
        Imgcodecs.imwrite(imageLocationForMarvin, thresholdImage);

        // reading the image from LocalFileSystem as
        // MarvinImage. (This is the image that was the output of previous class that was written to LocalFileSystem)
        MarvinImage boundedBoxImage = MarvinImageIO.loadImage(imageLocationForMarvin);
        try {
            // Storing the result of findText function in bounded_box_image
            findText(boundedBoxImage, 30, 20, 100, 170, outputAfterMarvin);
        } catch (Exception e) {
            System.out.println(e);
        }
        MarvinImageIO.saveImage(boundedBoxImage, outputAfterMarvin); // Saving the output of this class to LocalFileSystem

    }

    public static MarvinImage findText(MarvinImage boundedBoxImage, int maxWhiteSpace, int maxFontLineWidth,
                                       int minTextWidth, int grayScaleThreshold, String outputAfterMarvin) {
        List<MarvinSegment> segments = findTextRegions(boundedBoxImage, maxWhiteSpace, maxFontLineWidth, minTextWidth, grayScaleThreshold);

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
                boundedBoxImage.drawRect(s.x1, s.y1, s.x2 - s.x1, s.y2 - s.y1, Color.red);
                boundedBoxImage.drawRect(s.x1 + 1, s.y1 + 1, (s.x2 - s.x1) - 2, (s.y2 - s.y1) - 2, Color.red);
                boundedBoxImage.drawRect(s.x1 + 2, s.y1 + 2, (s.x2 - s.x1) - 4, (s.y2 - s.y1) - 4, Color.red);

                /*
                 * drawRect -> draws the outline of a rectangle
                 * x1 -> x coordinate of rectangle that is to be drawn,
                 * y1 -> y coordinate of rectangle that is to be drawn,
                 * x2 -> width of rectangle to be drawn,
                 * y2 -> height of rectangle to be drawn
                 */
            }
        }

        return boundedBoxImage;
    }


}
