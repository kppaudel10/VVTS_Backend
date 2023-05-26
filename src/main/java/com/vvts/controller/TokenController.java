package com.vvts.controller;

import com.vvts.config.jwt.UserDataConfig;
import com.vvts.service.VehicleService;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @auther kul.paudel
 * @created at 2023-05-26
 */

@RequestMapping("/api/pincode")
@RequiredArgsConstructor
public class TokenController {

    private final MessageSource messageSource;

    private final VehicleService vehicleService;

    private final UserDataConfig userDataConfig;


    @GetMapping("/generate")
    public GlobalApiResponse savePinCode(Authentication authentication) throws EmailException {
        return new GlobalApiResponse(messageSource.getMessage("data.save", null, null), true,
                vehicleService.generateValidationToken(userDataConfig.getLoggedInUserId(authentication)));
    }

    @GetMapping("/validate")
    public GlobalApiResponse validatePinCode(@RequestParam("pinCode") String pinCode, Authentication authentication) throws EmailException {
        return new GlobalApiResponse(messageSource.getMessage("data.save", null, null), true,
                vehicleService.validatePincode(pinCode, userDataConfig.getLoggedInUserId(authentication)));
    }

}
