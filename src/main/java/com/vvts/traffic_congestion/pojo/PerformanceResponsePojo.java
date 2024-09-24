package com.vvts.traffic_congestion.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author kul.paudel
 * @created at 2024-09-24
 */
@Getter
@Setter
public class PerformanceResponsePojo {
    private Integer index;
    private Double predictedTraffic;
    private Double estimateTraffic;
    private Double actualTraffic;

}
