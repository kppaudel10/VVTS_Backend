package com.vvts.traffic_congestion.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @auther kul.paudel
 * @created at 2024-09-20
 */
public interface TrainingService {
    Object trainData(MultipartFile dataFile, Integer kValue) throws IOException;

    void writeTrainDataIntoLog(String content);

    void analyzeKNN(Integer kValue);

    double predictTraffic(String loc, int day, int timeinterval);
}
