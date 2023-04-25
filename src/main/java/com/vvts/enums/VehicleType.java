package com.vvts.enums;

import com.vvts.dto.VehicleTypePojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
public enum VehicleType {

    CAR(0), MOTORCYCLE(1);

    VehicleType(int i) {
    }

    public static List<VehicleTypePojo> getVehicleTypeList() {
        List<VehicleTypePojo> vehicleTypeList = new ArrayList<>();
        for (VehicleType vehicleType : Arrays.asList(VehicleType.values())) {
            VehicleTypePojo vehicleTypePojo = new VehicleTypePojo();
            vehicleTypePojo.setVehicleTypeKey(vehicleType.ordinal());
            vehicleTypePojo.setVehicleTypeValue(vehicleType.name());
            vehicleTypeList.add(vehicleTypePojo);
        }
        return vehicleTypeList;
    }

    public static VehicleType getVehicleTypeKey(Integer key) {
        for (VehicleType vehicleType : Arrays.asList(VehicleType.values())) {
            if (vehicleType.ordinal() == key) {
                return vehicleType;
            }
        }
        return null;
    }

}
