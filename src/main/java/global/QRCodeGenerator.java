package global;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther kul.paudel
 * @created at 2023-05-24
 */
public class QRCodeGenerator {

    public static String getQrCode(String qrBody, String imageName, Integer loginUseId) throws IOException {
        String text = "Hello, World!";// The content of the QR code
        String ipAddress = getIPAddress();
        if (ipAddress != null) {
            text = ipAddress.concat(":8848/api/public-user/".concat(String.valueOf(loginUseId)));
        }

        int width = 300; // The width of the QR code
        int height = 300; // The height of the QR code
        String format = "png"; // The format of the QR code image

        String uploadDir = "";
        Path uploadPath = null;
        // create folder if not already not exists
        uploadDir = System.getProperty("user.home").concat("/vvts/qr_code");
        uploadPath = Paths.get(uploadDir);
        // create upload file directory if already not exists
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filePath = String.valueOf(uploadPath).concat("/".concat(imageName));       // The file path to save the QR code image

        // Generate the QR code matrix
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        // Set QR code encoding parameters
        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L); // Error correction level (L: 7%, M: 15%, Q: 25%, H: 30%)
        hintMap.put(EncodeHintType.MARGIN, 1); // White border margin size, 1 is the smallest value

        try {
            bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hintMap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        // Create a BufferedImage from the BitMatrix (you can use any image library you prefer)
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int value = bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF; // Set black or white pixels
                image.setRGB(x, y, value);
            }
        }

        // Save the image to a file
        try {
            ImageIO.write(image, format, new File(filePath));
            System.out.println("QR code image generated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    private static String getIPAddress() {
        String ipAddress = null;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();

                    // Filter out loopback and link-local addresses
                    if (!address.isLoopbackAddress() && !address.isLinkLocalAddress()) {
                        ipAddress = address.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error: Failed to retrieve the IP address.");
            e.printStackTrace();
        }
        return ipAddress;
    }
}
