package com.vvts.service.impl;

import com.vvts.dto.LicenseDto;
import com.vvts.dto.LicenseResponseDto;
import com.vvts.entity.License;
import com.vvts.enums.LicenseCategory;
import com.vvts.enums.VehicleType;
import com.vvts.projection.LicenseProjection;
import com.vvts.repo.LicenseRepo;
import com.vvts.repo.UsersRepo;
import com.vvts.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @auther kul.paudel
 * @created at 2023-04-17
 */
@Service
@RequiredArgsConstructor
public class LicenseServiceImpl implements LicenseService {

    private final LicenseRepo licenseRepo;

    private final UsersRepo usersRepo;

    @Override
    public LicenseDto saveLicense(LicenseDto licenseDto) throws ParseException {
        // check citizenship number is valid or not
        if (usersRepo.getVerifiedCitizenshipCount(licenseDto.getCitizenshipNo()) == 0) {
            throw new RuntimeException("User not found with provided Citizenship number: " + licenseDto.getCitizenshipNo());
        }

        // check citizenship number already exits or not
        if (licenseRepo.getCitizenshipNumberCount(licenseDto.getCitizenshipNo(), licenseDto.getLicenseCategory()) > 0) {
            throw new RuntimeException("User with Citizenship number : " + licenseDto.getCitizenshipNo() +
                    " already has license of " + VehicleType.getVehicleTypeKey(licenseDto.getLicenseCategory()).name());
        }
        // build entity
        License license = License.builder()
                .id(licenseDto.getId())
                .citizenshipNo(licenseDto.getCitizenshipNo())
                .district(licenseDto.getDistrict())
                .licenseCategory(LicenseCategory.getLicenseCategoryByKey(licenseDto.getLicenseCategory()))
                .validDate(new SimpleDateFormat("yyyy-MM-dd").parse(licenseDto.getValidDate())).build();
        if (licenseDto.getId() == null) {
            license.setLicenseNo(generateLicenseNumber());
        } else {
            license.setLicenseNo(licenseDto.getLicenseNo());
        }
        // save entity
        license = licenseRepo.save(license);
        licenseDto.setId(license.getId());
        licenseDto.setLicenseNo(license.getLicenseNo());
        return licenseDto;

    }

    @Override
    public Boolean deleteLicense(Integer id) {
        Boolean result = true;
        try {
            licenseRepo.deleteById(id);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    @Override
    public List<LicenseResponseDto> getAllLicenseList(String searchValue, Pageable pageable) {
        List<LicenseProjection> licenseProjectionList;
        if (searchValue != null) {
            String concatWithLike = "%".concat(searchValue).concat("%");
            licenseProjectionList = licenseRepo.filterLicenseDetails(concatWithLike, pageable);

        } else {
            String concatWithLike = "%".concat("--1").concat("%");
            licenseProjectionList = licenseRepo.filterLicenseDetails(concatWithLike, pageable);
        }
        List<LicenseResponseDto> licenseResponseList = new ArrayList<>();
        for (LicenseProjection licenseProjection : licenseProjectionList) {
            licenseResponseList.add(convertLicenseProjectionToDto(licenseProjection));
        }

        return licenseResponseList;
    }

    private String generateLicenseNumber() {
        Random random = new Random();
        int num1 = random.nextInt(100);
        int num2 = random.nextInt(100);
        int num3 = random.nextInt(100000000);
        String value = String.format("%02d-%02d-%08d", num1, num2, num3);

        // check give value is already exits or not
        if (licenseRepo.getCountOfLicenseByLicenseNumber(value) > 0) {
            // again generate new once until unique is not generate
            generateLicenseNumber();
        }
        return value;
    }

    private LicenseResponseDto convertLicenseProjectionToDto(LicenseProjection licenseProjection) {
        return LicenseResponseDto.builder()
                .id(licenseProjection.getId())
                .licenseNo(licenseProjection.getLicenseNo())
                .validDate(licenseProjection.getValidDate())
                .LicenseCategoryName(LicenseCategory.getLicenseCategoryValueByKey(licenseProjection.getLicenseCategory()))
                .district(licenseProjection.getDistrict())
                .licensedOwnUserName(licenseProjection.getLicensedUserName())
                .citizenshipNo(licenseProjection.getCitizenshipNo())
                .build();
    }
}
