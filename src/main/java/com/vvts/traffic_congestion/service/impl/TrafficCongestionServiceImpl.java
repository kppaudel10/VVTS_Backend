package com.vvts.traffic_congestion.service.impl;

import com.vvts.config.AppException;
import com.vvts.dto.traffic_congestion.TrafficForecastDataPojo;
import com.vvts.dto.traffic_congestion.TrafficForecastRequestPojo;
import com.vvts.traffic_congestion.pojo.*;
import com.vvts.traffic_congestion.service.TrafficCongestionService;
import com.vvts.traffic_congestion.utils.FileAppender;
import com.vvts.traffic_congestion.utils.LocationTraffic;
import com.vvts.traffic_congestion.utils.MyDistanceSort;
import com.vvts.utiles.ImageUtils;
import com.vvts.utiles.ImageValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @auther kul.paudel
 * @created at 2024-09-20
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TrafficCongestionServiceImpl implements TrafficCongestionService {

    private final ImageUtils imageUtils;
    private final ImageValidation imageValidation;
    private HashMap allLocationTraffic = new HashMap();
    private ArrayList<LocationTraffic> locationTrafficVector = new ArrayList<>();
    private final FileAppender fileAppender;
    private final StringBuilder logs = new StringBuilder();
    private final LogData logData = new LogData();
    private final Logs processLogs = new Logs();
    private final List<SpatialCorrelation> spatialCorrelationList = new ArrayList<>();
    private final Forecasting forecastingData = new Forecasting();

    static int convertToDay(String date) {
        // 04/02/2017
        Calendar cal = Calendar.getInstance();
        String[] pa = date.split("/");
        int year = Integer.parseInt(pa[2]);
        int months = Integer.parseInt(pa[1]);
        int days = Integer.parseInt(pa[0]);
        cal.set(year, months, days);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    public LogData trainData(MultipartFile dataFileMultipartFile, Integer kValue) throws IOException {
        String dataFile = getFilePath(dataFileMultipartFile);
        Vector<String> totrec = new Vector<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(dataFile);
            DataInputStream in = new DataInputStream(fileInputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int count = 0;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                if (count == 0) {
                    count++;
                    continue;
                }
                totrec.add(strLine);
                count++;
            }
            br.close();
            in.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
        int trainRows = (int) (totrec.size() * 0.80);
        List<String> storingLocationList = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(dataFile);
            DataInputStream in = new DataInputStream(fileInputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int count = 0;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                if (count == 0) {
                    count++;
                    continue;
                }
                if (count == trainRows) {
                    break;
                }
                log.info("Processing" + strLine);
                //#latitude,longtitude,day,timeinterval,traffic
                String[] parts = strLine.split(",");

                String t = parts[0] + "#" + parts[1];

                LocationTraffic lt = (LocationTraffic) allLocationTraffic.get(t);
                int day = Integer.parseInt(parts[2]);
                int ti = Integer.parseInt(parts[3]);
                double traff = Double.parseDouble(parts[4]);
                if (lt == null) {
                    lt = new LocationTraffic();
                    lt.location = t;
                    lt.latitude = Double.parseDouble(parts[0]);
                    lt.longitude = Double.parseDouble(parts[1]);

                    if (traff > lt.allDayTraffic[day].getTrafficRate()[ti]) {
                        lt.allDayTraffic[day].getTrafficRate()[ti] = traff;
                    }
                    writeTrainDataIntoLog("Storing location <" + t + ">");
                    // storing into location
                    String storingLocation = lt.latitude + " , " + lt.longitude;
                    storingLocationList.add(storingLocation);

                    allLocationTraffic.put(t, lt);
                } else {
                    if (traff > lt.allDayTraffic[day].getTrafficRate()[ti]) {
                        lt.allDayTraffic[day].getTrafficRate()[ti] = traff;
                    }
                }
            }
            processLogs.setStoringLocations(storingLocationList);

            br.close();
            in.close();
            fileInputStream.close();

            analyzeKNN(kValue);

            // measure accuracy
            int c = 1;
            for (int i = trainRows; i < totrec.size(); i++) {
                //#latitude,longtitude,day,timeinterval,traffic
                String[] parts = totrec.get(i).split(",");
                String loc = parts[0] + "#" + parts[1];
                int d = Integer.parseInt(parts[2]);
                int ti = Integer.parseInt(parts[3]);
                double eval;
                if (parts[4].split("\\.").length == 2) {
                    eval = Double.parseDouble(String.format("%.2f", parts[4]));
                } else {
                    eval = Double.parseDouble(parts[4]);
                }
                double aval = predictTraffic(loc, d, ti);
                double aval2 = aval * 2.5;
                String cth = "P#" + c + "#" + aval;
                fileAppender.appendToFile("Perfg1.txt", cth);
                cth = "E#" + c + "#" + aval2;
                fileAppender.appendToFile("Perfg1.txt", cth);
                cth = "A#" + c + "#" + eval;
                fileAppender.appendToFile("Perfg1.txt", cth);
                c++;
            }
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
        logData.setLogs(processLogs);
        return logData;
    }

    @Override
    public void analyzeKNN(Integer kValue) {
        locationTrafficVector = new ArrayList<>();
        Collection ct = allLocationTraffic.values();
        for (Object o : ct) {
            LocationTraffic lt = (LocationTraffic) o;
            locationTrafficVector.add(lt);
        }
        for (int i = 0; i < locationTrafficVector.size(); i++) {
            LocationTraffic x = locationTrafficVector.get(i);
            Vector<LocationTraffic> others = new Vector<>();
            for (int j = 0; j < locationTrafficVector.size(); j++) {
                if (i == j) continue;

                LocationTraffic y = locationTrafficVector.get(j);
                y.dis = Math.sqrt(Math.pow(x.latitude - y.longitude, 2) + Math.pow(x.longitude - y.longitude, 2));
                others.add(y);
            }
            Collections.sort(others, new MyDistanceSort());
            writeTrainDataIntoLog("!!!! The spatial correlation values Locaiton " + x.latitude + "," + x.longitude);
            // add into logs
            SpatialCorrelation spatialCorrelation = new SpatialCorrelation();
            spatialCorrelation.setLocation(x.latitude + " , " + x.longitude);
            List<String> correlations = new ArrayList<>();
            for (int m = 0; m < kValue; m++) {
                locationTrafficVector.get(i).locationTraffics.add(others.get(m));
                writeTrainDataIntoLog(others.get(m).latitude + "," + others.get(m).longitude);
                correlations.add(others.get(m).latitude + "#" + others.get(m).longitude);
            }
            spatialCorrelation.setCorrelations(correlations);
            spatialCorrelationList.add(spatialCorrelation);
            writeTrainDataIntoLog("!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        processLogs.setSpatialCorrelation(spatialCorrelationList);
    }

    @Override
    public double predictTraffic(String loc, int day, int timeInterval) {
        for (LocationTraffic locationTraffic : locationTrafficVector) {
            if (locationTraffic.location.equals(loc)) {
                log.info("Mathced in location :{}", loc);
                //prediction of traffic
                //temporal traffic
                double temp = locationTraffic.allDayTraffic[day].getTrafficRate()[timeInterval];
                log.info("Temporal traffic:{}", temp);
                double[] spatialCorrelation = new double[locationTraffic.locationTraffics.size()];
                double spattot = 0;
                for (int j = 0; j < locationTraffic.locationTraffics.size(); j++) {
                    spatialCorrelation[j] = locationTraffic.
                            locationTraffics.get(j).allDayTraffic[day].getTrafficRate()[timeInterval];
                    spattot = spattot + spatialCorrelation[j];
                }
                log.info("Spatial traffic:{}", spattot);
                temp = temp + spattot;
                temp = temp / (spatialCorrelation.length + 1);
                log.info("Spatio temporal is {}", temp);
                return temp;
            }
        }
        log.info("Location not matched");
        return -1;

    }

    @Override
    public List<TrafficForecastDataPojo> getTrafficForecastData(TrafficForecastRequestPojo trafficForecastRequestPojo) {
        String date = trafficForecastRequestPojo.getDate();
        Integer timeInterval = trafficForecastRequestPojo.getTimeInterval();
        int d = convertToDay(date);
        String[] pa = date.split("/");
        int y = Integer.parseInt(pa[2]);
        int m = Integer.parseInt(pa[1]);
        int da = Integer.parseInt(pa[0]);

        forecastingData.setDate(date);
        forecastingData.setDay(d);
        forecastingData.setTimeInterval(timeInterval.toString());
        writeTrainDataIntoLog("!!!!!!!!!! Forecasting called with "
                + date + " and day=" + d + " time:" + timeInterval);
        Vector<TrafficForecastDataPojo> trafficForecastDataList = new Vector<>();
        try {
            // get textFile
            String directionFile = getFilePath(trafficForecastRequestPojo.getDirectionFile());
            FileInputStream fstream = new FileInputStream(directionFile);

            DataInputStream in = new DataInputStream(fstream);

            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            List<Prediction> predictionList = new ArrayList<>();
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                Prediction prediction = new Prediction();
                writeTrainDataIntoLog("Trying to predict for " + strLine);
                String[] parts = strLine.split("#");
                prediction.setLocation(parts[0] + "#" + parts[1]);
                TrafficForecastDataPojo trafficForecastData = new TrafficForecastDataPojo();
                trafficForecastData.setLatitude(parts[0]);
                trafficForecastData.setLongitude(parts[1]);
                String location = parts[0] + "#" + parts[1];
                double res = predictTraffic(location, d, timeInterval);
                trafficForecastData.setTraffic((int) res);

                trafficForecastDataList.add(trafficForecastData);
                prediction.setTraffic(res);
                writeTrainDataIntoLog("Predicted traffic for loc:" + strLine + "=" + res);
                try {
          /*          Database db = new Database();
                    String place = (String)locmap.get(strLine);
                    String q = "delete from trafficinfo where loclatlong='" + place + "' and timeval='" +ti +"' and " +
                            " dateval='" + dastr + "'";
                    System.out.println(q);
                    db.executeUpdate(q);
                    q = "insert into trafficinfo values('" + place + "','" + ti + "'," + res + ",'" + dastr +"')";
                    System.out.println(q);
                    db.executeUpdate(q);
                    db.close();*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
                predictionList.add(prediction);
            }
            forecastingData.setPredictions(predictionList);
//            displayInMap(allres);
            br.close();
            in.close();
            fstream.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AppException(ex.getMessage());
        }
        processLogs.setForecasting(forecastingData);
        return trafficForecastDataList;
    }

    @Override
    public LogData getTrafficCongestionLogs() {
        return logData;
    }

    @Override
    public List<PerformanceResponsePojo> getForecastPerformance() throws IOException {
        List<PerformanceResponsePojo> performanceList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Perfg1.txt"))) {
            String line;
            PerformanceResponsePojo trafficPerformance = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("#");
                switch (parts[0]) {
                    case "P":
                        trafficPerformance = new PerformanceResponsePojo();
                        trafficPerformance.setIndex(Integer.parseInt(parts[1]));
                        trafficPerformance.setPredictedTraffic(Double.parseDouble(parts[2]));
                        break;
                    case "E":
                        if (trafficPerformance != null) {
                            trafficPerformance.setEstimateTraffic(Double.parseDouble(parts[2]));
                        }
                        break;
                    case "A":
                        if (trafficPerformance != null) {
                            trafficPerformance.setActualTraffic(Double.parseDouble(parts[2]));
                            performanceList.add(trafficPerformance);  // Store the completed entry
                        }
                        break;
                }
            }
        }
        return performanceList;
    }

    @Override
    public void writeTrainDataIntoLog(String content) {
        log.info("logs----------------:::::" + content);
        logs.append(content);
    }

    private String getFilePath(MultipartFile file) throws IOException {
        if (file != null) {
            String fileExtension = imageValidation.validateImage(file);
            // create folder if not already not exists
            String uploadDir = System.getProperty("user.home").concat("/vvts/training");
            Path uploadPath = Paths.get(uploadDir);
            // create upload file directory if already not exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // create full url
            Path filePath = uploadPath.resolve(imageUtils.getUniqueDataFileName("TrainingData", fileExtension));
            file.transferTo(filePath);
            return filePath.toString();
        }
        return "";
    }
}
