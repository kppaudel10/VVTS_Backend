package com.vvts.service.impl;

import com.vvts.dto.VehicleDto;
import com.vvts.entity.Users;
import com.vvts.entity.VehicleDetail;
import com.vvts.enums.VehicleType;
import com.vvts.repo.UsersRepo;
import com.vvts.repo.VehicleRepo;
import com.vvts.service.VehicleService;
import com.vvts.utiles.VINGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepo vehicleRepo;

    private final UsersRepo usersRepo;

    private final VINGenerator vinGenerator;

    @Override
    public VehicleDto saveVehicleDetail(VehicleDto vehicleDto, Integer loginUserId) {
        // get user by login user id
        Optional<Users> optionalUsers = usersRepo.findById(loginUserId);
        if (!optionalUsers.isPresent()) {
            throw new RuntimeException("User does not exits with user id : " + loginUserId);
        }
        Users users = optionalUsers.get();

        VehicleDetail vehicleDetail = VehicleDetail
                .builder().id(vehicleDto.getId())
                .manufactureYear(vehicleDto.getManufactureYear())
                .vehicleType(VehicleType.getVehicleTypeKey(vehicleDto.getVehicleType()))
                .vehicleIdentificationNo(vinGenerator.generateVIN(vehicleDto.getCompanyCode(),
                        vehicleDto.getVehicleType(), vehicleDto.getManufactureYear()))
                .companyCode(vehicleDto.getCompanyCode())
                .companyName(vehicleDto.getCompanyName())
                .vendor(users).build();
        // save
        vehicleDetail = vehicleRepo.save(vehicleDetail);
        vehicleDto.setId(vehicleDetail.getId());
        vehicleDto.setIdentificationNo(vehicleDetail.getVehicleIdentificationNo());
        return vehicleDto;
    }
}
