package com.vvts.utiles;

import com.vvts.entity.VehicleDetail;
import com.vvts.enums.VehicleType;
import com.vvts.repo.VehicleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
@Component
@RequiredArgsConstructor
public class VINGenerator {

    private final VehicleRepo vehicleRepo;

    public String generateVIN(String companyCode, Integer vehicleType, String manufactureYear) {

        VehicleDetail vehicleDetail = vehicleRepo.getVehicleDetailByCompanyCodeAndVehicleType(companyCode, vehicleType);
        String vehicleTypeName = Objects.requireNonNull(VehicleType.getVehicleTypeKey(vehicleType)).name();
        if (vehicleDetail != null) {
            // fetch series number of vehicle
            int codeAndTypeLength = companyCode.concat(vehicleTypeName).length();
            String seriesNumber = vehicleDetail.getVehicleIdentificationNo().substring(codeAndTypeLength);
            // increase VIN by 1
            Integer vinSeries = Integer.parseInt(seriesNumber) + 1;
            // create new VIN
            return companyCode.concat(vehicleTypeName).concat(String.valueOf(vinSeries));
        } else {
            return companyCode.concat(vehicleTypeName).concat(String.valueOf(manufactureYear)).concat("00000");
        }
    }

}
