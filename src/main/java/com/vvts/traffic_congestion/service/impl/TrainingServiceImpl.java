package com.vvts.traffic_congestion.service.impl;

import com.vvts.dto.traffic_congestion.TrafficForecastDataPojo;
import com.vvts.dto.traffic_congestion.TrafficForecastRequestPojo;
import com.vvts.traffic_congestion.service.TrainingService;
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
public class TrainingServiceImpl implements TrainingService {

    private final ImageUtils imageUtils;
    private final ImageValidation imageValidation;
    private HashMap allLocationTraffic = new HashMap();
    private Vector<LocationTraffic> locationTrafficVector = new Vector<>();
    private final FileAppender fileAppender;

    static int convertToDay(String date) {
        // 04/02/2017
        Calendar cal = Calendar.getInstance();
        String[] pa = date.split("/");
        int y = Integer.parseInt(pa[2]);
        int m = Integer.parseInt(pa[1]);
        int d = Integer.parseInt(pa[0]);

        cal.set(y, m, d);
        int val = cal.get(Calendar.DAY_OF_WEEK);

        //System.out.println(val);
        return val;
    }

    @Override
    public Object trainData(MultipartFile dataFileMultipartFile, Integer kValue) throws IOException {
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
            throw new RuntimeException(e.getMessage());
        }
        int trainRows = (int) (totrec.size() * 0.80);
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
                    allLocationTraffic.put(t, lt);
                } else {
                    if (traff > lt.allDayTraffic[day].getTrafficRate()[ti]) {
                        lt.allDayTraffic[day].getTrafficRate()[ti] = traff;
                    }
                }
            }

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
                double eval = Double.parseDouble(parts[4]);
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
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void analyzeKNN(Integer kValue) {
        locationTrafficVector = new Vector<>();
        Collection ct = allLocationTraffic.values();
        Iterator it = ct.iterator();
        while (it.hasNext()) {
            LocationTraffic lt = (LocationTraffic) it.next();
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
            for (int m = 0; m < kValue; m++) {
                locationTrafficVector.get(i).locationTraffics.add(others.get(m));
                writeTrainDataIntoLog(others.get(m).latitude + "," + others.get(m).longitude);
            }
            writeTrainDataIntoLog("!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    @Override
    public double predictTraffic(String loc, int day, int timeInterval) {
        for (int i = 0; i < locationTrafficVector.size(); i++) {
            //writetolog("Mathching against location :" + vloctraffic.get(i).loc);
            if (locationTrafficVector.get(i).location.equals(loc)) {
                System.out.println("Mathced in location :" + loc);
                //prediction of traffic
                //temporal traffic
                double temp = locationTrafficVector.get(i).allDayTraffic[day].getTrafficRate()[timeInterval];
                System.out.println("Temporal traffic:" + temp);
                double[] spatico = new double[locationTrafficVector.get(i).locationTraffics.size()];
                double spattot = 0;
                for (int j = 0; j < locationTrafficVector.get(i).locationTraffics.size(); j++) {
                    spatico[j] = locationTrafficVector.get(i).
                            locationTraffics.get(j).allDayTraffic[day].getTrafficRate()[timeInterval];
                    spattot = spattot + spatico[j];
                }
                System.out.println("Spatial traffic:" + spattot);
                temp = temp + spattot;
                temp = temp / (spatico.length + 1);
                System.out.println("Spatio temporal is " + temp);
                return temp;
            }
        }
        System.out.println("Location not matched");
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
        String dastr = da + "-" + m + "-" + y;

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
            int count = 0;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                writeTrainDataIntoLog("Trying to predict for " + strLine);
                String[] parts = strLine.split("#");

                TrafficForecastDataPojo trafficForecastData = new TrafficForecastDataPojo();
                trafficForecastData.setLatitude(parts[0]);
                trafficForecastData.setLongitude(parts[1]);
                double res = predictTraffic(strLine, d, timeInterval);
                trafficForecastData.setTraffic((int) res);

                trafficForecastDataList.add(trafficForecastData);
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

            }
//            displayInMap(allres);
            br.close();
            in.close();
            fstream.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return trafficForecastDataList;
    }

    @Override
    public void writeTrainDataIntoLog(String content) {
        log.info("logs----------------:::::" + content);
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
