package com.vvts.service;

import com.vvts.dto.BlueBookDto;
import com.vvts.dto.VehicleTypePojo;
import com.vvts.entity.BlueBook;
import com.vvts.projection.BlueBookProjection;

import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
public interface BlueBookService {

    List<VehicleTypePojo> getAllVehicleType();
    BlueBookDto saveBlueBook(BlueBookDto blueBookDto);
    List<BlueBookProjection> filterBlueBook(String searchData);

}
