package com.vvts.controller;

import com.vvts.dto.TaxClearanceDto;
import com.vvts.service.TaxClearanceService;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @auther kul.paudel
 * @created at 2023-08-08
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tax-clearance/")
public class TaxClearanceController {

    private final TaxClearanceService taxClearanceService;

    private final MessageSource messageSource;

    @PostMapping("/save")
    public GlobalApiResponse saveTaxClearance(@RequestBody TaxClearanceDto taxClearanceDto) throws IOException {
        return new GlobalApiResponse(messageSource.getMessage("data.save", null, null), true,
                taxClearanceService.saveTaxClearance(taxClearanceDto));
    }
}
