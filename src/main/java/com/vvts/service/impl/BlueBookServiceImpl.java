package com.vvts.service.impl;

import com.vvts.dto.BlueBookDto;
import com.vvts.dto.VehicleTypePojo;
import com.vvts.entity.BlueBook;
import com.vvts.enums.VehicleType;
import com.vvts.projection.BlueBookProjection;
import com.vvts.repo.BlueBookRepo;
import com.vvts.repo.UsersRepo;
import com.vvts.repo.VehicleRepo;
import com.vvts.service.BlueBookService;
import com.vvts.utiles.NumberPlateGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    private final VehicleRepo vehicleRepo;

    private final UsersRepo usersRepo;

    private final NumberPlateGenerator numberPlateGenerator;


    @Override
    public List<VehicleTypePojo> getAllVehicleType() {
        return VehicleType.getVehicleTypeList();
    }

    @Override
    public BlueBookDto saveBlueBook(BlueBookDto blueBookDto) {

        //check identification number is valid or not
        if (vehicleRepo.getCountByVIN(blueBookDto.getVehicleIdentificationNo()).equals(0)) {
            throw new RuntimeException("Vehicle not found with provided identification number: " + blueBookDto.getVehicleIdentificationNo());
        }
        // check citizenship number is valid or not
        if (usersRepo.getVerifiedCitizenshipCount(blueBookDto.getCitizenshipNo()) == 0) {
            throw new RuntimeException("User not found with provided citizenship number: " + blueBookDto.getCitizenshipNo());
        }
        /*
        check data already exits or not with same data
         */
        BlueBook savedBlueBook = blueBookRepo.getDuplicateDataCount(blueBookDto.getCitizenshipNo(), String.valueOf(LocalDate.now()),
                blueBookDto.getVehicleIdentificationNo(), blueBookDto.getVehicleType());

        if (savedBlueBook == null) {
            BlueBook blueBook = BlueBook.builder()
                    .id(blueBookDto.getId())
                    .citizenshipNo(blueBookDto.getCitizenshipNo())
                    .effectiveDate(new Date())
                    .numberPlate(removeUnWantCharAndGetNewOnce(blueBookDto.getNumberPlate()))
                    .vehicleType(VehicleType.getVehicleTypeKey(blueBookDto.getVehicleType()))
                    .vehicleIdentificationNo(blueBookDto.getVehicleIdentificationNo())
                    .build();
            //save
            blueBook = blueBookRepo.save(blueBook);
            blueBookDto.setId(blueBook.getId());
            blueBookDto.setVehicleIdentificationNo(blueBook.getVehicleIdentificationNo());
        } else {
            blueBookDto.setId(savedBlueBook.getId());
            blueBookDto.setVehicleIdentificationNo(savedBlueBook.getVehicleIdentificationNo());
        }
        return blueBookDto;
    }

    @Override
    public List<BlueBookProjection> filterBlueBook(String searchData) {
        if (searchData != null) {
            String searchValueWithLike = "%".concat(searchData).concat("%");
            return blueBookRepo.getBlueBookData(searchValueWithLike);
        } else {
            String searchValueWithLike = "%".concat("--1").concat("%");
            return blueBookRepo.getBlueBookData(searchValueWithLike);
        }
    }

    @Override
    public List<BlueBookProjection> getBlueBookByUserId(Integer userId) {
        return blueBookRepo.getBlueBookDataByUserId(userId);
    }

    private String getCompanyCode(String vehicleIdentificationNo) {
        String[] strings = vehicleIdentificationNo.split("-");
        if (strings.length >= 1) {
            return strings[0];
        } else {
            throw new RuntimeException("Unable to find company code");
        }
    }

    private String removeUnWantCharAndGetNewOnce(String string) {
        // remove space
        string = string.replaceAll("\\s", "");
        // remove \n if it contain
        String finalString = "";
        for (String spr : string.split("n")) {
            if (String.valueOf(spr.charAt(spr.length() - 1)).equals("\\")) {
                finalString = finalString.concat(spr.substring(0, spr.length() - 1));
            } else {
                finalString = finalString.concat(spr);
            }
        }

        return finalString;
    }


}

