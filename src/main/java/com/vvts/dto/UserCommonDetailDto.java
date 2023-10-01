package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCommonDetailDto {

    private String citizenshipNo;

    private Set<String> vehicleIdentificationNo;

    private Set<String> numberPlate;

}
