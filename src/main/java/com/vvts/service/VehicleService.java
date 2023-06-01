package com.vvts.service;

import com.vvts.dto.*;
import com.vvts.projection.BuyRequestProjection;
import com.vvts.projection.BuyerRequestProjection;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.mail.EmailException;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
public interface VehicleService {
    VehicleDto saveVehicleDetail(VehicleDto vehicleDto, Integer loginUserId);

    BuyRequestPojo saveVehicleBuyRequest(BuyRequestPojo buyRequestPojo, Integer loginUserId) throws MessagingException, EmailException;

    List<BuyRequestResponseDto> getBuyRequestList(Integer loginUserId);

    NumberPlateScannerResponsePojo getScanNumberPlate(MultipartFile scanImage, String destinationLanguage) throws IOException, TesseractException;

    boolean generateValidationToken(BuyRequestPojo buyRequestPojo, Integer loginUserId) throws EmailException;

    Boolean validatePincode(String pinCode, Integer loginUserId);
    List<BuyerRequestProjection> getBuyRequestOfLoginUser(Integer loginUserId);

    String takeActionOnSellRequest(SellRequestActionPojo sellRequestActionPojo);

    List<Map<String,Object>> getVehicleByVendorId(Integer vendorId);
    List<OwnershipResponseDto> getOwnershipRequestList();

}
