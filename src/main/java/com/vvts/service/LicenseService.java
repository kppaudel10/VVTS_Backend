package com.vvts.service;

import com.vvts.dto.LicenseDto;
import com.vvts.projection.LicenseProjection;

import java.text.ParseException;
import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2023-04-17
 */
public interface LicenseService {

    LicenseDto saveLicense(LicenseDto licenseDto) throws ParseException;

    Boolean deleteLicense(Integer id);

    List<LicenseProjection> getAllLicenseList();


}
