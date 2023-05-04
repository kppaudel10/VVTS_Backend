package com.vvts.utiles;

import com.vvts.entity.VehicleDetail;
import com.vvts.repo.BlueBookRepo;
import com.vvts.repo.VehicleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @auther kul.paudel
 * @created at 2023-05-04
 */
@Component
@RequiredArgsConstructor
public class NumberPlateGenerator {

    private static final String[] CAR_PREFIXES = {"बा", "झ", "क", "को", "न", "प", "प्ब", "पे", "प्र"};
    private static final String[] BIKE_PREFIXES = {"बा", "भ", "गा", "को", "म", "प", "रा"};
    private final VehicleRepo vehicleRepo;

    private final BlueBookRepo blueBookRepo;

    public String getNumberPlate(String companyCode, Integer vehicleType) {

        String numberPlate = null;
        VehicleDetail vehicleDetail = vehicleRepo.getVehicleDetailByCompanyCodeAndVehicleType(companyCode, vehicleType);

        Random rand = new Random();
        String prefix, number = null;

        if (vehicleDetail.getVehicleType().name().equalsIgnoreCase("car")) {
            // Generate a car number plate
            prefix = CAR_PREFIXES[rand.nextInt(CAR_PREFIXES.length)];
            number = String.format("%04d", rand.nextInt(10000));
            numberPlate = "बा ".concat("2 ".concat(prefix.concat(" "))).concat(number);
        }
        if (vehicleDetail.getVehicleType().name().equalsIgnoreCase("bike")) {
            // Generate a bike number plate
            prefix = BIKE_PREFIXES[rand.nextInt(BIKE_PREFIXES.length)];

            number = String.format("%03d", rand.nextInt(1000));
            numberPlate = "बा ".concat("2 ".concat(prefix.concat(" "))).concat(number);
        }
        // check generate number plate already exits or not
        if (blueBookRepo.getCountNumberPlateExits(numberPlate) > 1) {
            // generate new once
            getNumberPlate(companyCode, vehicleType);
        }
        return numberPlate;
    }
}
