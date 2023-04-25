package com.vvts.controller;

import com.vvts.service.BlueBookService;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/vehicle-type")
    public GlobalApiResponse getVehicleType() {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                blueBookService.getAllVehicleType());
    }
}
