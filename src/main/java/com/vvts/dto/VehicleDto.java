package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto {

    private Integer id;

    private String manufactureYear;

    private String identificationNo;

    private Integer vehicleType;

}
