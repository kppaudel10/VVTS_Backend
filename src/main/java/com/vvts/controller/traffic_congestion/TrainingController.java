package com.vvts.controller.traffic_congestion;

import com.vvts.dto.traffic_congestion.TrafficForecastRequestPojo;
import com.vvts.dto.traffic_congestion.TrainingRequestPojo;
import com.vvts.traffic_congestion.service.TrainingService;
import com.vvts.utiles.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/data")
    public GlobalApiResponse trainTrafficData(@Valid @ModelAttribute TrainingRequestPojo trainingRequestPojo) throws IOException {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                trainingService.trainData(trainingRequestPojo.getTrainingDataFile(), trainingRequestPojo.getKValue()));
    }

    @PostMapping("/forecast/data")
    public GlobalApiResponse getForecast(@Valid @ModelAttribute TrafficForecastRequestPojo trafficForecastRequestPojo) throws IOException {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                trainingService.getTrafficForecastData(trafficForecastRequestPojo));
    }

    @GetMapping("/logs")
    public GlobalApiResponse getLogs() {
        return new GlobalApiResponse(messageSource.getMessage("data.fetch", null, null), true,
                trainingService.getTrafficCongestionLogs());
    }

}
