package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther kul.paudel
 * @created at 2023-06-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OwnershipResponseDto {
    private Integer id;
    private String requestDate;
    private String vehicleIdentificationNo;
    private String companyCode;
    private String manufactureYear;
    private Integer vehicleType;
    private String buyerName;
    private String buyerEmail;
    private String buyerMobileNumber;
    private String buyerAddress;
    private String buyerProfileUrl;
    private String buyerCitizenshipNo;
    private String buyerCitizenshipFontUrl;
    private String buyerCitizenshipBackUrl;
    private String sellerName;
    private String sellerEmail;
    private String sellerMobileNumber;
    private String sellerAddress;
    private String sellerProfileUrl;
    private String sellerCitizenshipNo;
    private String sellerCitizenshipFontUrl;
    private String sellerCitizenshipBackUrl;

}
