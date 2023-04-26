package com.vvts.controller;

import com.vvts.config.jwt.UserDataConfig;
import com.vvts.dto.PublicUserBasicDataDto;
import com.vvts.dto.UserKycUpdateDto;
import com.vvts.service.UsersService;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */
@RestController
@RequestMapping("/api/public-user")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    private final MessageSource messageSource;

    private final UserDataConfig userDataConfig;

    @PostMapping("/save")
    private GlobalApiResponse savePublicUser(@RequestBody PublicUserBasicDataDto publicUserBasicDataDto) throws IOException {
        return new GlobalApiResponse(messageSource.getMessage("user.save", null, null), true,
                usersService.savePublicUser(publicUserBasicDataDto));
    }

    @PostMapping("/kyc-update")
    private GlobalApiResponse updateUserKyc(@Valid @ModelAttribute UserKycUpdateDto userKycUpdateDto, Authentication authentication) throws IOException {
        Integer userId = userDataConfig.getLoggedInUserId(authentication);
        userKycUpdateDto.setUserId(userId);
        return new GlobalApiResponse(messageSource.getMessage("user.kyc", null, null), true,
                usersService.updateUserKyc(userKycUpdateDto));
    }

}
