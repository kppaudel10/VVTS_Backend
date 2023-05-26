package com.vvts.service;

import com.vvts.dto.BuyRequestPojo;
import com.vvts.dto.NumberPlateScannerResponsePojo;
import com.vvts.dto.VehicleDto;
import com.vvts.projection.BuyRequestProjection;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.mail.EmailException;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
public interface VehicleService {
    VehicleDto saveVehicleDetail(VehicleDto vehicleDto, Integer loginUserId);

    BuyRequestPojo saveVehicleBuyRequest(BuyRequestPojo buyRequestPojo, Integer loginUserId) throws MessagingException, EmailException;

    List<BuyRequestProjection> getBuyRequestList(Integer loginUserId);

    NumberPlateScannerResponsePojo getScanNumberPlate(MultipartFile scanImage, String destinationLanguage) throws IOException, TesseractException;

    boolean generateValidationToken(Integer loginUserId) throws EmailException;
    Boolean validatePincode(String pinCode, Integer loginUserId);


}
