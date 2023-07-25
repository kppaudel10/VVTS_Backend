package com.vvts.projection;

/**
 * @auther kul.paudel
 * @created at 2023-05-04
 */
public interface NumberPlateScannerProjection {

    String getUserId();

    String getName();

    String getContact();

    String getEmail();

    String getAddress();

    String getCitizenshipNo();

    String getProfileImageUrl();

    String getLicenseNo();

    String getLicenseValidDate();

    String getBlueBookNo();

    String getBlueBookValidDate();

    Boolean getIsLicenseValid();

    String getBlueBookEffectiveDate();
    String getVehicleIdentificationNo();

    String getManufactureYear();

    String getVehicleCompanyName();

}
