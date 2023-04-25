package com.vvts.service;

import com.vvts.dto.VehicleDto;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
public interface VehicleService {
    VehicleDto saveVehicleDetail(VehicleDto vehicleDto,Integer loginUserId);

}
