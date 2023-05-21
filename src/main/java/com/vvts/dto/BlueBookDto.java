package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlueBookDto {

    private Integer id;

    @NotNull(message = "CitizenshipNo must be required")
    private String citizenshipNo;

    @NotNull(message = "Vehicle Type must be required")
    private int vehicleType;

    @NotNull(message = "Vehicle Identification No must be required")
    private String vehicleIdentificationNo;

    @NotNull(message = "numberPlate must be required")
    private String numberPlate;



}
