package com.vvts.traffic_congestion.utils;

import java.util.Vector;

/**
 * @auther kul.paudel
 * @created at 2024-09-20
 */

public class LocationTraffic {
    public double latitude;
    public double longitude;
    public String location;
    public Vector<LocationTraffic> locationTraffics = new Vector<>();
    public DayTraffic[] allDayTraffic = new DayTraffic[7];
    public double dis;

    public LocationTraffic() {
        for (int i = 0; i < allDayTraffic.length; i++) {
            allDayTraffic[i] = new DayTraffic();
        }
    }
}
