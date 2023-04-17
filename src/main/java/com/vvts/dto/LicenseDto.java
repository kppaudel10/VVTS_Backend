package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @auther kul.paudel
 * @created at 2023-04-17
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LicenseDto {

    private Integer id;

    private String licenseNo;

    private String citizenshipNo;

    private String validDate;

    private String district;

}
