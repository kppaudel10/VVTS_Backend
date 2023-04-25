package com.vvts.service;

import com.vvts.dto.BlueBookDto;
import com.vvts.dto.VehicleTypePojo;

import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
public interface BlueBookService {

    List<VehicleTypePojo> getAllVehicleType();
    BlueBookDto saveBlueBook(BlueBookDto blueBookDto);

}
