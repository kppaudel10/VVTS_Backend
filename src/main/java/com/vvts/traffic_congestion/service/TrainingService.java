package com.vvts.traffic_congestion.service;

import com.vvts.dto.traffic_congestion.TrafficForecastDataPojo;
import com.vvts.dto.traffic_congestion.TrafficForecastRequestPojo;
import com.vvts.traffic_congestion.pojo.LogData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2024-09-20
 */
public interface TrainingService {

    LogData trainData(MultipartFile dataFile, Integer kValue) throws IOException;

    void writeTrainDataIntoLog(String content);

    void analyzeKNN(Integer kValue);

    double predictTraffic(String loc, int day, int timeInterval);

    List<TrafficForecastDataPojo> getTrafficForecastData(TrafficForecastRequestPojo trafficForecastRequestPojo) throws IOException;

    LogData getTrafficCongestionLogs();

}
