package com.vvts.controller;

import com.vvts.config.jwt.UserDataConfig;
import com.vvts.dto.PublicUserBasicDataDto;
import com.vvts.dto.UserKycUpdateDto;
import com.vvts.service.UsersService;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
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

    @PostMapping(value = "/kyc-update")
    private GlobalApiResponse updateUserKyc(@Valid @ModelAttribute UserKycUpdateDto userKycUpdateDto, Authentication authentication, HttpServletRequest request) throws IOException {
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

    @GetMapping("/active/list")
    public GlobalApiResponse getActiveUserList() throws IOException {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                usersService.getActiveUserList());
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
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName, @PathVariable String imageType) throws IOException {
        // Load the image file from the classpath
        String imagePath = "";
        if (imageType.equalsIgnoreCase("profile")) {
            imagePath = System.getProperty("user.home").concat("/vvts/profile/").concat(imageName);
        } else if (imageType.equalsIgnoreCase("citizen")) {
            imagePath = System.getProperty("user.home").concat("/vvts/citizen/").concat(imageName);
        } else if (imageType.equalsIgnoreCase("taxClearance")) {
            imagePath = System.getProperty("user.home").concat("/vvts/tax-clearance/").concat(imageName);
        }
        return getResponseByImagePath(imagePath);
    }

    /*
    this api is used to fetch profile picture of login user
     */
    @GetMapping("/profile-picture")
    public ResponseEntity<byte[]> getProfilePictureOfLoginUser(Authentication authentication) throws IOException {
        String imagePath = usersService.getProfileImagePathOfLoginUser(userDataConfig.getLoggedInUserId(authentication));
        if (imagePath != null) {
            return getResponseByImagePath(imagePath);
        } else {
            return null;
        }
    }

    private ResponseEntity<byte[]> getResponseByImagePath(String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        FileInputStream fis = new FileInputStream(imageFile);
        byte[] imageBytes = fis.readAllBytes();

        // Set the response headers
        fis.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imageBytes.length);
        return ResponseEntity.ok().headers(headers).body(imageBytes);
    }

    /**
     * This API is used to generate the qr code of login user
     *
     * @param authentication
     * @return
     */
    @GetMapping("/qr-code-generate")
    public ResponseEntity<byte[]> generateQrCodeOfLoginUser(Authentication authentication) throws IOException {
        String imagePath = usersService.getGenerateQrCode(userDataConfig.getLoggedInUserId(authentication));
        return getResponseByImagePath(imagePath);
    }

    @GetMapping("/qr-code/download")
    public ResponseEntity<Resource> downloadQrCode(Authentication authentication) {
        return usersService.downloadImage(userDataConfig.getLoggedInUserId(authentication));
    }

    @GetMapping("/detail/{id}")
    public GlobalApiResponse getPublicUserDetailById(@PathVariable("id") Integer id) {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                usersService.getUserById(id));
    }

    @GetMapping("/common-detail")
    public GlobalApiResponse getLoginUserCommonDetail(Authentication authentication) {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                usersService.getUserDetail(userDataConfig.getLoggedInUserId(authentication)));
    }
}

