package com.vvts.controller;

import com.vvts.config.jwt.UserDataConfig;
import com.vvts.dto.BuyRequestPojo;
import com.vvts.dto.VehicleDto;
import com.vvts.service.VehicleService;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    private final UserDataConfig userDataConfig;

    private final MessageSource messageSource;

    @PostMapping("/save")
    public GlobalApiResponse saveVehicleDetail(@Valid @RequestBody VehicleDto vehicleDto, Authentication authentication) {
        return new GlobalApiResponse(messageSource.getMessage("data.save", null, null), true,
                vehicleService.saveVehicleDetail(vehicleDto, userDataConfig.getLoggedInUserId(authentication)));
    }

    @PostMapping("/buy-request")
    public GlobalApiResponse vehicleBuyRequest(@Valid @RequestBody BuyRequestPojo buyRequestPojo, Authentication authentication) throws MessagingException {
        return new GlobalApiResponse(messageSource.getMessage("buy.request", null, null), true,
                vehicleService.saveVehicleBuyRequest(buyRequestPojo, userDataConfig.getLoggedInUserId(authentication)));
    }


}
