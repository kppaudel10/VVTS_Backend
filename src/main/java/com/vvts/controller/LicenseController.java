package com.vvts.controller;

import com.vvts.dto.LicenseDto;
import com.vvts.dto.PublicUserDto;
import com.vvts.service.LicenseService;
import com.vvts.service.PublicUserService;
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
@RequestMapping("/api/license/")
public class LicenseController {

    private final LicenseService licenseService;

    private final MessageSource messageSource;

    @PostMapping("/save")
    private GlobalApiResponse saveLicenseDetail(@RequestBody LicenseDto licenseDto) throws ParseException {
        return new GlobalApiResponse(messageSource.getMessage("license.save",null,null),
                true,licenseService.saveLicense(licenseDto));
    }

    @DeleteMapping("/delete/{id}")
    private GlobalApiResponse deleteLicense(@PathVariable Integer id ) throws ParseException {
        return new GlobalApiResponse(messageSource.getMessage("license.delete",null,null),
                true,licenseService.deleteLicense(id));
    }

}
