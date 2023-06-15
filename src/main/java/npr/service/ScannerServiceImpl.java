package npr.service;

import com.vvts.entity.ScanImage;
import lombok.RequiredArgsConstructor;
import npr.repo.ScanImageRepo;
import npr.utils.step.ImageUtils;
import npr.utils.step.ImageValidation;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @auther kul.paudel
 * @created at 2023-06-03
 */
@Service
@RequiredArgsConstructor
public class ScannerServiceImpl implements ScannerService {

    private static final String HASH_ALGORITHM = "MD5";
    private final ImageValidation imageValidation;
    private final ImageUtils imageUtils;

    private final ScanImageRepo scanImageRepo;

    private static String calculateImageHash(BufferedImage image) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] imageBytes = getBytesFromImage(image);
            byte[] hashBytes = md.digest(imageBytes);

            StringBuilder hashBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hashBuilder.append(String.format("%02x", b));
            }
            return hashBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BufferedImage loadImageFromFile(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] getBytesFromImage(BufferedImage image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();
            return imageBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<?> sacnNumberPlate(MultipartFile multipartFile) throws Exception {
        String filePathName = saveAndScanImage(multipartFile);
//        return new OcrProcessor().doOcr(filePathName);
        ResponseEntity<?> response = numberPlateScannerTest(filePathName);
        return response;
    }

    @Override
    public ScanImage saveScanImage(ScanImage scanImage) {
        return scanImageRepo.save(scanImage);
    }

    private String saveAndScanImage(MultipartFile scanImage) throws
            IOException {
        // first validate number plate image extension
        String ppExtension = imageValidation.validateImage(scanImage);

        String uploadDir = "";
        Path uploadPath = null;
        Path scanImageFilePath;
        String scanImageName = generateScanImageUniqueName(ppExtension);
        // create folder if not already not exists
        uploadDir = System.getProperty("user.home").concat("/vvts/scan_image");
        uploadPath = Paths.get(uploadDir);
        // create upload file directory if already not exists
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        // create full url and save image
        scanImageFilePath = uploadPath.resolve(scanImageName);
        scanImage.transferTo(scanImageFilePath);

        // save into scan image table after successfully save
        return String.valueOf(scanImageFilePath);
    }

    private String generateScanImageUniqueName(String imageExtension) {
        while (true) {
            String scanImageName = imageUtils.generateUniqueImageName(imageUtils.generateRandomString(),
                    imageUtils.generateRandomInt(), "scan_image", imageExtension);
            // check scan image name is already exists or not
            if (scanImageRepo.countScanImageName(scanImageName) == 0) {
                return scanImageName;
            }
        }
    }

    private ResponseEntity<?> numberPlateScannerTest(String imageFilePath) {
        /* check already scan image or not if already exists then fetch data from database
         first find the current input image hash value and compare hash value to old once
         */
        // Load the newly arrived image
        BufferedImage newImage = loadImageFromFile(imageFilePath);
        String newImageHash = calculateImageHash(newImage);
        // search hashValue on database
        ScanImage scanImage = scanImageRepo.getScanImageByScanImageHasValue(newImageHash);
        if (scanImage != null) {
            return new ResponseEntity<>(scanImage.getScanResult(), HttpStatus.OK);
        } else {
            // then scan new scan image into database
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("9bad4672-0acb-11ee-b832-627a75b3435d:".getBytes()));

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(imageFilePath));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            String url = "https://app.nanonets.com/api/v2/OCR/Model/62bbf466-bb22-4778-91b1-a5fbfa789568/LabelFile/?async=false";

            return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        }
    }

}

