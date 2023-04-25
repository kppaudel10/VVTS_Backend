package com.vvts.service.impl;

import com.vvts.dto.BlueBookDto;
import com.vvts.dto.VehicleTypePojo;
import com.vvts.entity.BlueBook;
import com.vvts.enums.VehicleType;
import com.vvts.repo.BlueBookRepo;
import com.vvts.service.BlueBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
@Service
@RequiredArgsConstructor
public class BlueBookServiceImpl implements BlueBookService {

    private final BlueBookRepo blueBookRepo;

    @Override
    public List<VehicleTypePojo> getAllVehicleType() {
        return VehicleType.getVehicleTypeList();
    }

    @Override
    public BlueBookDto saveBlueBook(BlueBookDto blueBookDto) {
        BlueBook blueBook = BlueBook.builder()
                .id(blueBookDto.getId())
                .citizenshipNo(blueBookDto.getCitizenshipNo())
                .effectiveDate(new Date())
                .vehicleType(VehicleType.getVehicleTypeKey(blueBookDto.getVehicleType()))
                .vehicleIdentificationNo(blueBookDto.getVehicleIdentificationNo())
                .build();
        //save
        blueBook = blueBookRepo.save(blueBook);
        blueBookDto.setId(blueBook.getId());
        blueBookDto.setVehicleIdentificationNo(blueBook.getVehicleIdentificationNo());
        return blueBookDto;
    }

}

