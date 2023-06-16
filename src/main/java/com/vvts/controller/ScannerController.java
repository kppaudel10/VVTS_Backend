package com.vvts.controller;

import lombok.RequiredArgsConstructor;
import com.vvts.service.ScannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @auther kul.paudel
 * @created at 2023-06-03
 */
@RestController
@RequestMapping("/api/number-plate")
@RequiredArgsConstructor
public class ScannerController {

    private final ScannerService scannerService;

    @PostMapping("/scan")
    public ResponseEntity<?> sacnNumberPlate(@ModelAttribute MultipartFile scanImage) throws Exception {
        return scannerService.sacnNumberPlate(scanImage);
    }
}
