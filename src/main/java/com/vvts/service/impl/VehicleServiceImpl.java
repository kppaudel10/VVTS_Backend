package com.vvts.service.impl;

import com.vvts.dto.BuyRequestPojo;
import com.vvts.dto.VehicleDto;
import com.vvts.entity.OwnershipTransfer;
import com.vvts.entity.Users;
import com.vvts.entity.VehicleDetail;
import com.vvts.enums.VehicleType;
import com.vvts.repo.OwnershipTransferRepo;
import com.vvts.repo.UsersRepo;
import com.vvts.repo.VehicleRepo;
import com.vvts.service.VehicleService;
import com.vvts.utiles.VINGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    private final OwnershipTransferRepo ownershipTransferRepo;

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

    @Override
    public BuyRequestPojo saveVehicleBuyRequest(BuyRequestPojo buyRequestPojo, Integer loginUserId) {
        // first check the owner name and mobile number are valid or not
        Users users = usersRepo.getUserMobileNumberCount(buyRequestPojo.getOwnerMobileNumber());
        if (users == null) {
            throw new RuntimeException("Insufficient Owner information.");
        }
        if (!users.getName().equalsIgnoreCase(buyRequestPojo.getOwnerName())) {
            throw new RuntimeException("Invalid Owner name: "+buyRequestPojo.getOwnerName());
        }
        // check vehicle identification number is exists or not
        Integer existenceCount = vehicleRepo.getVehicleDetailByVINAndNumber(buyRequestPojo.getVehicleIdentificationNo(),
                users.getCitizenshipNo());
        if (existenceCount == null || existenceCount.equals(0)) {
            throw new RuntimeException("Invalid Vehicle identification number: "+buyRequestPojo.getVehicleIdentificationNo());
        }
        // build entity
        Users buyer = new Users();
        buyer.setId(loginUserId);
        OwnershipTransfer ownershipTransfer = OwnershipTransfer.builder()
                .transferRequestDate(new Date())
                .seller(users)
                .buyer(buyer).build();
        ownershipTransferRepo.save(ownershipTransfer);
        return buyRequestPojo;
    }
}
