package com.vvts.traffic_congestion.utils;
import java.util.Comparator;
/**
 * @auther kul.paudel
 * @created at 2024-09-20
 */
public class MyDistanceSort implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        LocationTraffic l1 = (LocationTraffic)o1;
        LocationTraffic l2 = (LocationTraffic)o2;
        if (l1.dis<l2.dis)
        {
            return -1;
        }
        return 1;
    }
}
