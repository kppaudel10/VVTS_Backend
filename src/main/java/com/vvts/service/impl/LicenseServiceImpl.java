package com.vvts.service.impl;

import com.vvts.dto.LicenseDto;
import com.vvts.entity.License;
import com.vvts.repo.LicenseRepo;
import com.vvts.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
        // build entity
        License license = License.builder()
                .id(licenseDto.getId())
                .licenseNo(licenseDto.getLicenseNo())
                .citizenshipNo(licenseDto.getCitizenshipNo())
                .district(licenseDto.getDistrict())
                .validDate(new SimpleDateFormat("yyyy-MM-dd").parse(licenseDto.getValidDate())).build();
        // save entity
        license = licenseRepo.save(license);
        licenseDto.setId(license.getId());
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
}
