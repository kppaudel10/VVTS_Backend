package com.vvts.controller;

import com.vvts.dto.PublicUserBasicDataDto;
import com.vvts.service.PublicUserService;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */
@RestController
@RequestMapping("/api/public-user")
@RequiredArgsConstructor
public class PublicUserController {

    private final PublicUserService publicUserService;

    @PostMapping("/save")
    private GlobalApiResponse savePublicUser(@RequestBody PublicUserBasicDataDto publicUserBasicDataDto) throws IOException {
        return new GlobalApiResponse("User Save Successfully",true,publicUserService.savePublicUser(publicUserBasicDataDto));
    }
}
