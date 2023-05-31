package com.vvts.projection;

/**
 * @auther kul.paudel
 * @created at 2023-05-01
 */
public interface BuyRequestProjection {

    Integer getId();

    Boolean getIsApproveByAdmin();

    Boolean getIsApproveByOwner();

    String getApproveDate();

    String getRequestDate();

    String getVehicleIdentificationNo();

    String getCompanyCode();

    String getManufactureYear();

    Integer getVehicleType();

    String getBuyerName();

    String getBuyerMobileNumber();

    String getBuyerAddress();

    String getBuyerEmail();

    String getBuyerProfileUrl();

    String getBuyerCitizenshipNo();

    String getBuyerCitizenshipFontUrl();

    String getBuyerCitizenshipBackUrl();

}
