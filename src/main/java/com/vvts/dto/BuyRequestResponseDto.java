package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther kul.paudel
 * @created at 2023-05-28
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyRequestResponseDto {

    private Integer id;

    private String requestDate;

    private Integer vehicleType;

    private String vehicleIdentificationNo;

    private String companyCode;

    private String manufactureYear;

    private String buyerName;

    private String buyerMobileNumber;

    private String buyerAddress;

    private String buyerProfilePictureUrl;

    private String buyerEmail;

}
