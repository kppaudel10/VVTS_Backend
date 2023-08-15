package com.vvts.projection;

public interface TaxClearanceProjection {

    String getProcessDate();

    String getCitizenshipNo();

    Integer getVehicleType();

    String getIdentificationNo();

    String getNumberPlate();

    Double getPaidAmount();

    String getValidDate();

    Boolean getIsVerified();

}
