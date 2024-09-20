package com.vvts.traffic_congestion.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * @auther kul.paudel
 * @created at 2024-08-06
 */
@Getter
@Setter
public class DayTraffic {
     double[] trafficRate = new double[23];
    DayTraffic() {
        Arrays.fill(trafficRate, -1);

    }
}
