package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

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

    @NotNull(message = "Citizenship No must be required.")
    private String citizenshipNo;

    @NotNull(message = "Valid date must be required.")
    private String validDate;

    @NotNull(message = "District must be required.")
    private String district;

}
