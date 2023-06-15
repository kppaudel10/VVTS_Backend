package npr.service;

import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.TesseractException;
import npr.utils.step.ImageUtils;
import npr.utils.step.ImageValidation;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * @auther kul.paudel
 * @created at 2023-06-03
 */
@Service
@RequiredArgsConstructor
public class    ScannerServiceImpl implements ScannerService {

    private final ImageValidation imageValidation;

    private final ImageUtils imageUtils;

    @Override
    public ResponseEntity<?> sacnNumberPlate(MultipartFile multipartFile) throws Exception {
        String filePathName = saveAndScanImage(multipartFile);
//        return new OcrProcessor().doOcr(filePathName);
        ResponseEntity<?> response = numberPlateScannerTest(filePathName);
        return response;
    }

    private String saveAndScanImage(MultipartFile scanImage) throws
            IOException, TesseractException {
        // first validate number plate image extension
        String ppExtension = imageValidation.validateImage(scanImage);

        String uploadDir = "";
        Path uploadPath = null;
        Path scanImageFilePath;
        if (scanImage != null) {
            String scanImageName = imageUtils.generateUniqueImageName(imageUtils.generateRandomString(),
                    imageUtils.generateRandomInt(), "scan_image", ppExtension);
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
            return String.valueOf(scanImageFilePath);
        }
        return null;
    }

    private ResponseEntity<?> numberPlateScannerTest(String imageFilePath) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("9bad4672-0acb-11ee-b832-627a75b3435d:".getBytes()));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(imageFilePath));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String url = "https://app.nanonets.com/api/v2/OCR/Model/62bbf466-bb22-4778-91b1-a5fbfa789568/LabelFile/?async=false";

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return response;
    }
}

