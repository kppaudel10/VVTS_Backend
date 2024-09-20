package com.vvts.controller.traffic_congestion;

import com.vvts.dto.traffic_congestion.TrainingRequestPojo;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vvts.traffic_congestion.service.TrainingService;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @auther kul.paudel
 * @created at 2024-09-20
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/traffic-congestion/training")
public class TrainingController {

    private final TrainingService trainingService;

    private final MessageSource messageSource;


    @GetMapping("/data")
    public GlobalApiResponse getVehicleType(@Valid @ModelAttribute TrainingRequestPojo trainingRequestPojo) throws IOException {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                trainingService.trainData(trainingRequestPojo.getTrainingDataFile(), trainingRequestPojo.getKValue()));
    }
}
