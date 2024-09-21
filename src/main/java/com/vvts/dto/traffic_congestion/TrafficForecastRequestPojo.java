package com.vvts.dto.traffic_congestion;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class TrafficForecastRequestPojo {
    private MultipartFile directionFile;
    private String date;
    private Integer timeInterval;
}
