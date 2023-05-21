package com.vvts.controller;

import com.vvts.config.jwt.UserDataConfig;
import com.vvts.dto.PublicUserBasicDataDto;
import com.vvts.dto.UserKycUpdateDto;
import com.vvts.service.UsersService;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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

    @PostMapping(value = "/kyc-update")
    private GlobalApiResponse updateUserKyc(@Valid @ModelAttribute UserKycUpdateDto userKycUpdateDto,
                                            Authentication authentication, HttpServletRequest request) throws IOException {
        Integer userId = userDataConfig.getLoggedInUserId(authentication);
        userKycUpdateDto.setUserId(userId);
        return new GlobalApiResponse(messageSource.getMessage("user.kyc", null, null), true,
                usersService.updateUserKyc(userKycUpdateDto));
    }

    @GetMapping("/kyc-request")
    public GlobalApiResponse getNewKycRequest() throws IOException {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                usersService.getNewKycRequest());
    }

    @GetMapping("/basic-detail")
    public GlobalApiResponse getUserBasicDetail(Authentication authentication) {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                usersService.getUserByUserId(userDataConfig.getLoggedInUserId(authentication)));
    }

    @GetMapping("/kyc-action/{userId}/{actionType}")
    public GlobalApiResponse actionOnKycRequest(@PathVariable Integer userId, @PathVariable String actionType) {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                usersService.getTakeActionOnKycRequest(userId, actionType));
    }


    @GetMapping("/getUserImage/{imageType}/{imageName}")
    public GlobalApiResponse getImage(@PathVariable String imageName, @PathVariable String imageType) throws IOException {
        // Load the image file from the classpath
        String imagePath = "";
        if (imageType.equalsIgnoreCase("profile")) {
            imagePath = System.getProperty("user.home").concat("/vvts/profile/").concat(imageName);
        } else if (imageType.equalsIgnoreCase("citizen")) {
            imagePath = System.getProperty("user.home").concat("/vvts/citizen/").concat(imageName);
        }
        File file = new File(imagePath);
        byte[] imageBytes = Files.readAllBytes(file.toPath());

        // Set the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE); // Adjust the media type based on the image format

//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(imageBytes);
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true, imageBytes);
    }

}
