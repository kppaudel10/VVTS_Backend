package com.vvts.service.impl;

import com.vvts.dto.VehicleTypePojo;
import com.vvts.enums.VehicleType;
import com.vvts.service.BlueBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
@Service
@RequiredArgsConstructor
public class BlueBookServiceImpl implements BlueBookService {

    @Override
    public List<VehicleTypePojo> getAllVehicleType() {
        return VehicleType.getVehicleTypeList();
    }
}
