package com.vvts.controller;

import com.vvts.config.jwt.UserDataConfig;
import com.vvts.dto.BuyRequestPojo;
import com.vvts.dto.SellRequestActionPojo;
import com.vvts.dto.VehicleDto;
import com.vvts.service.VehicleService;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.mail.EmailException;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

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
    public GlobalApiResponse vehicleBuyRequest(@Valid @RequestBody BuyRequestPojo buyRequestPojo, Authentication authentication)
            throws MessagingException, EmailException {
        return new GlobalApiResponse(messageSource.getMessage("buy.request", null, null), true,
                vehicleService.saveVehicleBuyRequest(buyRequestPojo, userDataConfig.getLoggedInUserId(authentication)));
    }

    @GetMapping("/buy-request/list")
    public GlobalApiResponse getVehicleBuyRequestList(Authentication authentication) {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                vehicleService.getBuyRequestOfLoginUser(userDataConfig.getLoggedInUserId(authentication)));
    }

    @GetMapping("/sell-request")
    public GlobalApiResponse vehicleBuyRequestList(Authentication authentication) {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                vehicleService.getBuyRequestList(userDataConfig.getLoggedInUserId(authentication)));
    }

    @PostMapping("/sell-request/action")
    public GlobalApiResponse takeActionOnSellRequest(@Valid @RequestBody SellRequestActionPojo sellRequestActionPojo) {
        return new GlobalApiResponse(messageSource.getMessage("data.update", null, null), true,
                vehicleService.takeActionOnSellRequest(sellRequestActionPojo));
    }

    @PostMapping("/number-plate/scan")
    public GlobalApiResponse getScanNumberPlate(@ModelAttribute MultipartFile numberPlateImage, @RequestParam String LanguageCode)
            throws TesseractException, IOException {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                vehicleService.getScanNumberPlate(numberPlateImage, LanguageCode));
    }


}
