package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther kul.paudel
 * @created at 2023-05-31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LicenseResponseDto {

    private Integer id;

    private String licenseNo;

    private String citizenshipNo;

    private String validDate;

    private String district;

    private String LicenseCategoryName;

    private String licensedOwnUserName;

}
