package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;



/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto {

    private Integer id;

    @NotNull(message = "manufactureYear must required")
    private String manufactureYear;

    private String identificationNo;

    @NotNull(message = "vehicleType must required")
    private Integer vehicleType;

    private String companyName;

    @NotNull(message = "Company code must required")
    private String companyCode;

    private Integer cc;

}
