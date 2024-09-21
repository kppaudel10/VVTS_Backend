package com.vvts.dto.traffic_congestion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrafficForecastDataPojo {
    private String latitude;
    private String longitude;
    private Integer traffic;
}
