package com.vvts.service;

import com.vvts.dto.BuyRequestPojo;
import com.vvts.dto.VehicleDto;
import com.vvts.projection.BuyRequestProjection;
import org.apache.commons.mail.EmailException;

import javax.mail.MessagingException;
import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
public interface VehicleService {
    VehicleDto saveVehicleDetail(VehicleDto vehicleDto, Integer loginUserId);

    BuyRequestPojo saveVehicleBuyRequest(BuyRequestPojo buyRequestPojo, Integer loginUserId) throws MessagingException, EmailException;

    List<BuyRequestProjection> getBuyRequestList(Integer loginUserId);
}
