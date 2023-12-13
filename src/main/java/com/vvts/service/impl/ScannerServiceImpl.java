package com.vvts.service.impl;

import com.vvts.dto.NumberPlateScannerResponsePojo;
import com.vvts.entity.BlueBook;
import com.vvts.entity.ScanImage;
import com.vvts.projection.NumberPlateScannerProjection;
import com.vvts.repo.BlueBookRepo;
import com.vvts.repo.ScanImageRepo;
import com.vvts.repo.VehicleRepo;
import com.vvts.service.ScannerService;
import com.vvts.utiles.ImageUtils;
import com.vvts.utiles.ImageValidation;
import lombok.RequiredArgsConstructor;
import npr.OcrProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
    private final VehicleRepo vehicleRepo;

    private final BlueBookRepo blueBookRepo;

    @Value("${image.fetch.api}")
    private String imageAccessBaseUrl;


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

    public static Result findMaxValueAndIndex(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("The list is null or empty.");
        }

        int max = Integer.MIN_VALUE;
        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            int current = list.get(i);
            if (current > max) {
                max = current;
                index = i;
            }
        }

        return new Result(index, max);
    }

    @Override
    public NumberPlateScannerResponsePojo sacnNumberPlate(MultipartFile multipartFile) throws Exception {
        List<String> scanImageDetail = saveAndScanImage(multipartFile);
        try {
//            new OcrProcessor().doOcr(scanImageDetail.get(1));
        } catch (Exception e) {
            System.out.println();
        }
        if (!scanImageDetail.isEmpty()) {
            process();
            String numberPlateOcrText = numberPlateScanner(scanImageDetail.get(0), scanImageDetail.get(1));
            if (numberPlateOcrText != null) {
                return getScannerResponse(numberPlateOcrText);
            } else {
                throw new RuntimeException("Please use appropriate image for detection");
            }
        }
        return null;
    }

    @Override
    public ScanImage saveScanImage(ScanImage scanImage) {
        return scanImageRepo.save(scanImage);
    }

    private List<String> saveAndScanImage(MultipartFile scanImage) throws
            IOException {
        // first validate number plate image extension
        String ppExtension = imageValidation.validateImage(scanImage);

        String uploadDir;
        Path uploadPath;
        Path scanImageFilePath;
        String scanImageName = generateScanImageUniqueName(ppExtension);
        // create folder if not already not exists
        uploadDir = System.getProperty("user.home").concat(File.separator).concat("vvts/scan_image");
        uploadPath = Paths.get(uploadDir);
        // create upload file directory if already not exists
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        // create full url and save image
        scanImageFilePath = uploadPath.resolve(scanImageName);
        scanImage.transferTo(scanImageFilePath);

        // save into scan image table after successfully save
        List<String> strings = new ArrayList<>();
        strings.add(scanImageName);
        strings.add(String.valueOf(scanImageFilePath));
        return strings;
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

    private String numberPlateScanner(String scanImageName, String imageFilePath) throws IOException {
        /* check already scan image or not if already exists then fetch data from database
         first find the current input image hash value and compare hash value to old once
         */
        // Load the newly arrived image
        BufferedImage newImage = loadImageFromFile(imageFilePath);
        String newImageHash = calculateImageHash(newImage);
        // search hashValue on database
        ScanImage scanImage = scanImageRepo.getScanImageByScanImageHasValue(newImageHash);
        if (scanImage != null) {
            System.out.println("------------------OCR OUTPUT --------------------------");
            System.out.println(scanImage.getScanResult());
            return scanImage.getScanResult();
        } else {
            // then scan new scan image into database
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            // english
//            headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("9bad4672-0acb-11ee-b832-627a75b3435d:".getBytes()));
            // mix up
//            headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("e872f3ca-0c00-11ee-93eb-7ac8f71b476b:".getBytes()));

            // new
            headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("e872f3ca-0c00-11ee-93eb-7ac8f71b476b:".getBytes()));

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(imageFilePath));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            //english
//            String url = "https://app.nanonets.com/api/v2/OCR/Model/31870eb5-8884-4fed-84a6-e92bc1b0859a/LabelFile/?async=false";

            // mix up
//            String url = "https://app.nanonets.com/api/v2/OCR/Model/72099705-4fac-4ca6-94f6-ab3986e3c929/LabelFile/?async=false";

            // new
            String url = "https://app.nanonets.com/api/v2/OCR/Model/1ead711d-b8bc-44a7-b165-38f8809dc384/LabelUrls/?async=false";

            String apiResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class).getBody();


            String ocrText = getOcrText(apiResponse);
            // save into scan image file
            scanImage = new ScanImage();
            scanImage.setScanImageName(scanImageName);
            scanImage.setScanImageURl(imageFilePath);
            scanImage.setScanImageHasValue(calculateImageHash(loadImageFromFile(imageFilePath)));
            scanImage.setScanResult(removeUnWantCharAndGetNewOnce(ocrText));

            scanImageRepo.save(scanImage);

            System.out.println("------------------OCR OUTPUT --------------------------");
            System.out.println(scanImage.getScanResult());
            return scanImage.getScanResult();
        }
    }

    private String getOcrText(String apiResponse) {
        for (String str : apiResponse.split(",")) {
            for (String str1 : str.split(":")) {
                if (str1.equals("\"ocr_text\"")) {
                    String ocrText = str.split(":")[1];
                    return ocrText.replaceAll("\"", "");
                }
            }
        }
        return null;
    }

    private NumberPlateScannerResponsePojo getScannerResponse(String scanOutput) {
        if (scanOutput != null) {
//            char[] scanOutputChars = scanOutput.toCharArray();
            String modifiedOutput = removeUnWantCharAndGetNewOnce(scanOutput);
            NumberPlateScannerProjection npcp = vehicleRepo.getUserAndVehicleDetailByNumberPlate(modifiedOutput);
            NumberPlateScannerResponsePojo scannerResponsePojo = new NumberPlateScannerResponsePojo();
            if (npcp != null) {
                scannerResponsePojo = getPojoByProjection(npcp);
                scannerResponsePojo.setOcrText(removeUnWantCharAndGetNewOnce(scanOutput));
                return scannerResponsePojo;
            } else {
                // check by matching the 75% char
                String ocrByMoreMatch = findClosedMatchNumberPlate(scanOutput);
                if (ocrByMoreMatch != null) {
                    npcp = vehicleRepo.getUserAndVehicleDetailByNumberPlate(modifiedOutput);
                    if (npcp != null) {
                        scannerResponsePojo = getPojoByProjection(npcp);
                        scannerResponsePojo.setOcrText(removeUnWantCharAndGetNewOnce(scanOutput));
                        return scannerResponsePojo;
                    } else {
                        scannerResponsePojo.setOcrText(scanOutput);
                        scannerResponsePojo.setName("Unknown");
                        return scannerResponsePojo;
                    }

                } else {
                    scannerResponsePojo.setOcrText(scanOutput);
                    scannerResponsePojo.setName("Unknown");
                    return scannerResponsePojo;
                }
            }
        }
        return null;
    }

    private NumberPlateScannerResponsePojo getPojoByProjection(NumberPlateScannerProjection npcp) {
        NumberPlateScannerResponsePojo scannerResponsePojo = new NumberPlateScannerResponsePojo();
        scannerResponsePojo.setName(npcp.getName());
        scannerResponsePojo.setUserId(npcp.getUserId());
        scannerResponsePojo.setAddress(npcp.getAddress());
        scannerResponsePojo.setContact(npcp.getContact());
        scannerResponsePojo.setEmail(npcp.getEmail());
        scannerResponsePojo.setProfileImageUrl(imageAccessBaseUrl.concat("/profile/")
                .concat(getFileNameFormPath(npcp.getProfileImageUrl())));
        scannerResponsePojo.setLicenseValidDate(npcp.getLicenseValidDate());
        scannerResponsePojo.setIsLicenseValid(npcp.getIsLicenseValid());
        scannerResponsePojo.setVehicleIdentificationNo(npcp.getVehicleIdentificationNo());
        scannerResponsePojo.setVehicleCompanyName(npcp.getVehicleCompanyName());
        scannerResponsePojo.setManufactureYear(npcp.getManufactureYear());
        scannerResponsePojo.setBlueBookEffectiveDate(npcp.getBlueBookEffectiveDate());
        return scannerResponsePojo;
    }

    private String findClosedMatchNumberPlate(String scanOutput) {
        String numberPlate = null;
        List<BlueBook> blueBookList = blueBookRepo.findAll();
        List<Integer> countList = new ArrayList<>();
        for (BlueBook blueBook : blueBookList) {
            Integer count = 0;
            if (blueBook.getNumberPlate() != null && blueBook.getNumberPlate().equals("")) {
                for (char blueBookChar : blueBook.getNumberPlate().toCharArray()) {
                    char[] chars = scanOutput.toCharArray();
                    for (char scanChar : chars) {
                        if (blueBookChar == scanChar) {
                            count++;
                            break;
                        }
                    }
                }
            }
            countList.add(count);
        }
        Result result = findMaxValueAndIndex(countList);
        // get blue book details by result index
        if (result != null && result.getIndex() != 0) {
            BlueBook blueBook = blueBookList.get(result.getIndex());
            // check how much number plate char match
            Integer matchPercentage = (Math.abs(blueBook.getNumberPlate().length() - result.getMaxValue()) * 100) /
                    blueBook.getNumberPlate().length();
            if (matchPercentage >= 75) {
                numberPlate = blueBook.getNumberPlate();
            }
        }
        return numberPlate;
    }

    private String removeUnWantCharAndGetNewOnce(String string) {
        // remove \n if it contain
        String finalString = "";
        if (string != null & !string.equals("")) {
            // remove space
            string = string.replaceAll("\\s", "");
            for (String spr : string.split("n")) {
                if (String.valueOf(spr.charAt(spr.length() - 1)).equals("\\")) {
                    finalString = finalString.concat(spr.substring(0, spr.length() - 1));
                } else {
                    finalString = finalString.concat(spr);
                }
            }
        }
        return finalString;
    }

    private String getFileNameFormPath(String filePath) {
        Path path = Paths.get(filePath);
        String fileName = path.getFileName().toString();
        return fileName;
    }

    private void process(){
        try {
            // Sleep for 3 seconds (3000 milliseconds)
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class Result {
    Integer index;
    Integer maxValue;

    public Result(Integer index, Integer maxValue) {
        this.index = index;
        this.maxValue = maxValue;
    }

    public Integer getIndex() {
        return index;
    }

    public Integer getMaxValue() {
        return maxValue;
    }
}

