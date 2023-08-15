package com.vvts.controller;

import com.vvts.config.jwt.UserDataConfig;
import com.vvts.dto.TaxClearanceDto;
import com.vvts.service.TaxClearanceService;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @auther kul.paudel
 * @created at 2023-08-08
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tax-clearance")
public class TaxClearanceController {

    private final TaxClearanceService taxClearanceService;

    private final MessageSource messageSource;

    private final UserDataConfig userDataConfig;

    @PostMapping("/save")
    public GlobalApiResponse saveTaxClearance(@ModelAttribute TaxClearanceDto taxClearanceDto) throws IOException {
        return new GlobalApiResponse(messageSource.getMessage("data.save", null, null), true,
                taxClearanceService.saveTaxClearance(taxClearanceDto));
    }

    @GetMapping("/List/loginUser")
    public GlobalApiResponse getTaxClearanceList(Authentication authentication) {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                taxClearanceService.getTaxClearanceListByUserId(userDataConfig.getLoggedInUserId(authentication)));
    }
}
