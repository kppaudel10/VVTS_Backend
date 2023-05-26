package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @auther kul.paudel
 * @created at 2023-04-28
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyRequestPojo {

    @NotNull(message = "Owner name must be required.")
    private String ownerName;

    @NotNull(message = "Owner mobile number must be required.")
    private String ownerMobileNumber;

    @NotNull(message = "Vehicle Identification number must required.")
    private String vehicleIdentificationNo;

}
