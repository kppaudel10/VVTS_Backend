package com.vvts.controller;

import com.vvts.config.jwt.UserDataConfig;
import com.vvts.dto.BlueBookDto;
import com.vvts.service.BlueBookService;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blue-book/")
public class BlueBookController {

    private final BlueBookService blueBookService;

    private final MessageSource messageSource;

    private final UserDataConfig userDataConfig;

    @GetMapping("/vehicle-type")
    public GlobalApiResponse getVehicleType() {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                blueBookService.getAllVehicleType());
    }

    @PostMapping("/save")
    public GlobalApiResponse saveBlueBook(@Valid @RequestBody BlueBookDto blueBookDto) {
        return new GlobalApiResponse(messageSource.getMessage("bluebook.save", null, null), true,
                blueBookService.saveBlueBook(blueBookDto));
    }

    @GetMapping("/list")
    public GlobalApiResponse getBlueBookList(@RequestParam(value = "searchValue", required = false) String searchValue) {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                blueBookService.filterBlueBook(searchValue));
    }

    @GetMapping("/list/login-user")
    public GlobalApiResponse getBlueBookBuLoginUserId(Authentication authentication) {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                blueBookService.getBlueBookByUserId(userDataConfig.getLoggedInUserId(authentication)));
    }
}
