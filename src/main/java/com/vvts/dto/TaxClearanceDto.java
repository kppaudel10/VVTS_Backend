package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @auther kul.paudel
 * @created at 2023-08-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaxClearanceDto {

    private Integer id;

    @NotNull(message = "Citizenship no must required !")
    @NotEmpty(message = "Citizenship no must required !")
    private String citizenshipNo;

    @NotNull(message = "Vehicle IdentificationNo must required !")
    @NotEmpty(message = "Vehicle IdentificationNo must required !")
    private String vehicleIdentificationNo;

//    @NotNull(message = "Numberplate must required !")
//    @NotEmpty(message = "Numberplate must required !")
    private String numberPlate;

    @NotNull(message = "Paid amount must required !")
    private Double paidAmount;

    @NotNull(message = "Amount paid bill doc must required !")
    private MultipartFile amountPaidSheet;

}
