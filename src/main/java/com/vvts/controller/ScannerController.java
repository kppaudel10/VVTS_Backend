package com.vvts.controller;

import com.vvts.service.ScannerService;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
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

    private final MessageSource messageSource;

    @PostMapping("/scan")
    public GlobalApiResponse sacnNumberPlate(@ModelAttribute MultipartFile scanImage) throws Exception {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                scannerService.sacnNumberPlate(scanImage));
    }
}
