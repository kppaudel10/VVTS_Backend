package com.vvts.projection;

public interface TaxClearanceProjection {

    Integer getId();

    String getProcessDate();

    String getCitizenshipNo();

    Integer getVehicleType();

    String getIdentificationNo();

    String getNumberPlate();

    Double getPaidAmount();

    String getValidYear();

    Boolean getIsVerified();
    Boolean getIsRejected();
    String getStatus();

    String getTaxPaidBillImageName();

}
