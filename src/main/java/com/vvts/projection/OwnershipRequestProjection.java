package com.vvts.projection;

/**
 * @auther kul.paudel
 * @created at 2023-06-01
 */
public interface OwnershipRequestProjection {
    Integer getId();

    String getRequestDate();

    String getVehicleIdentificationNo();

    String getCompanyCode();

    String getManufactureYear();

    Integer getVehicleType();

    String getBuyerName();

    String getBuyerEmail();

    String getBuyerMobileNumber();

    String getBuyerAddress();

    String getBuyerProfileUrl();

    String getBuyerCitizenshipNo();

    String getBuyerCitizenshipFontUrl();

    String getBuyerCitizenshipBackUrl();

    String getSellerName();

    String getSellerEmail();

    String getSellerMobileNumber();

    String getSellerAddress();

    String getSellerProfileUrl();

    String getSellerCitizenshipNo();

    String getSellerCitizenshipFontUrl();

    String getSellerCitizenshipBackUrl();

}
