package com.vvts.service;

import com.vvts.dto.TaxClearanceDto;

import java.io.IOException;

/**
 * @auther kul.paudel
 * @created at 2023-08-08
 */
public interface TaxClearanceService {

    TaxClearanceDto saveTaxClearance(TaxClearanceDto taxClearanceDto) throws IOException;
}
