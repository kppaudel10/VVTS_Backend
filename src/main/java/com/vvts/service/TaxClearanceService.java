package com.vvts.service;

import com.vvts.dto.TaxClearanceDto;
import com.vvts.projection.TaxClearanceProjection;
import com.vvts.projection.UserCommonDetailProjection;

import java.io.IOException;
import java.util.List;

/**;
 * @auther kul.paudel
 * @created at 2023-08-08
 */
public interface TaxClearanceService {

    TaxClearanceDto saveTaxClearance(TaxClearanceDto taxClearanceDto) throws IOException;

    List<TaxClearanceProjection> getTaxClearanceListByUserId(Integer loginUserId,Boolean isAll);

    String actionTaxClearanceRequest(String action,Integer taxClearanceId);
}
