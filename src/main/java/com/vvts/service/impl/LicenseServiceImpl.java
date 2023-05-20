package com.vvts.service.impl;

import com.vvts.dto.LicenseDto;
import com.vvts.entity.License;
import com.vvts.enums.VehicleType;
import com.vvts.projection.LicenseProjection;
import com.vvts.repo.LicenseRepo;
import com.vvts.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Override
    public LicenseDto saveLicense(LicenseDto licenseDto) throws ParseException {

        // check citizenship number already exits or not
        if (licenseRepo.getCitizenshipNumberCount(licenseDto.getCitizenshipNo()) > 0) {
            throw new RuntimeException("Citizenship number : " + licenseDto.getCitizenshipNo() + " already exists");
        }
        // build entity
        License license = License.builder()
                .id(licenseDto.getId())
                .citizenshipNo(licenseDto.getCitizenshipNo())
                .district(licenseDto.getDistrict())
                .vehicleType(VehicleType.getVehicleTypeKey(licenseDto.getVehicleType()))
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
    public List<LicenseProjection> getAllLicenseList(String searchValue, Pageable pageable) {
        if (searchValue != null) {
            return licenseRepo.filterLicenseDetails(searchValue, pageable);
        } else {
            return licenseRepo.filterLicenseDetails("-1", pageable);
        }
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
}
