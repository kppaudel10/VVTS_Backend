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
public class BlueBookDto {

    private Integer id;

    private String citizenshipNo;

    private int vehicleType;

    private String vehicleIdentificationNo;

}
