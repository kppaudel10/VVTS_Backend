package com.vvts.controller;

import com.vvts.dto.LicenseDto;
import com.vvts.service.LicenseService;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * @auther kul.paudel
 * @created at 2023-04-17
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/license/")
public class LicenseController {

    private final LicenseService licenseService;

    private final MessageSource messageSource;

    @PostMapping("/save")
    private GlobalApiResponse saveLicenseDetail(@RequestBody LicenseDto licenseDto) throws ParseException {
        return new GlobalApiResponse(messageSource.getMessage("license.save", null, null),
                true, licenseService.saveLicense(licenseDto));
    }

    @DeleteMapping("/delete/{id}")
    private GlobalApiResponse deleteLicense(@PathVariable Integer id) throws ParseException {
        return new GlobalApiResponse(messageSource.getMessage("license.delete", null, null),
                true, licenseService.deleteLicense(id));
    }

    @GetMapping("/list")
    private GlobalApiResponse getAllLicenseDetailsList(@RequestParam(value = "licenseNo", required = false) String licenseNo,
                                                       @RequestParam(value = "citizenshipNo", required = false) String citizenshipNo,
                                                       @RequestParam(value = "validDate", required = false) String validDate,
                                                       @RequestParam(value = "district",required = false) String district) {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null),
                true, licenseService.getAllLicenseList(licenseNo, citizenshipNo, validDate,district));
    }


}
