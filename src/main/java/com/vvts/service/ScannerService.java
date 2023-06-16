package com.vvts.service;

import com.vvts.entity.ScanImage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @auther kul.paudel
 * @created at 2023-06-03
 */
public interface ScannerService {

    ResponseEntity<?> sacnNumberPlate(MultipartFile multipartFile) throws Exception;

    ScanImage saveScanImage(ScanImage scanImage);

}
