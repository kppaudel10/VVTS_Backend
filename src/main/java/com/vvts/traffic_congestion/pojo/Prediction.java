package com.vvts.traffic_congestion.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author kul.paudel
 * @created at 2024-09-24
 */
@Getter
@Setter
public class Prediction {
    private String location;
    private double traffic;
}
