package com.vvts.projection;

/**
 * @auther kul.paudel
 * @created at 2023-05-26
 */
public interface BuyerRequestProjection {

    String getOwnerName();

    String getOwnerMobileNo();

    String getIdentificationNo();

    Integer getVehicleType();

    String getRequestDate();

    String getRequestStatus();

}
