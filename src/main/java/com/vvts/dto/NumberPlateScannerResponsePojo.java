package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther kul.paudel
 * @created at 2023-05-04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NumberPlateScannerResponsePojo {

    private String ocrText;

    private String name;

    private String address;

    private String email;

    private String userId;

    private String profileImageUrl;

    private String licenseValidDate;

    private Boolean isTaxClear;

    private String contact;

    private String blueBookEffectiveDate;

    private String vehicleIdentificationNo;

    private String manufactureYear;

    private String vehicleCompanyName;


}
