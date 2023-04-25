package com.vvts.controller;

import com.vvts.dto.PublicUserBasicDataDto;
import com.vvts.service.UsersService;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
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
public class UsersController {

    private final UsersService usersService;

    private final MessageSource messageSource;

    @PostMapping("/save")
    private GlobalApiResponse savePublicUser(@RequestBody PublicUserBasicDataDto publicUserBasicDataDto) throws IOException {
        return new GlobalApiResponse(messageSource.getMessage("user.save",null,null),true, usersService.savePublicUser(publicUserBasicDataDto));
    }
}