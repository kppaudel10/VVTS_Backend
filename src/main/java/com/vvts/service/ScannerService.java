package com.vvts.service;

import com.vvts.dto.NumberPlateScannerResponsePojo;
import com.vvts.entity.ScanImage;
import org.springframework.web.multipart.MultipartFile;

/**
 * @auther kul.paudel
 * @created at 2023-06-03
 */
public interface ScannerService {

    NumberPlateScannerResponsePojo sacnNumberPlate(MultipartFile multipartFile) throws Exception;

    ScanImage saveScanImage(ScanImage scanImage);

}
