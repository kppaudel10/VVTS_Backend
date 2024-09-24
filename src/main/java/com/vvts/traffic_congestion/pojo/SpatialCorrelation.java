package com.vvts.traffic_congestion.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author kul.paudel
 * @created at 2024-09-24
 */
@Getter
@Setter
public class SpatialCorrelation {

    private String location;
    private List<String> correlations;

}
