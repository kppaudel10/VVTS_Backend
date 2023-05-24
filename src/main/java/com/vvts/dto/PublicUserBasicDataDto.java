package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Normalized;

import javax.validation.constraints.NotNull;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicUserBasicDataDto {

    private Integer id;

    @NotNull(message = "Name must be required.")
    private String name;

    @NotNull(message = "Email must be required.")
    private String email;

    @NotNull(message = "Mobile number must be required.")
    private String mobileNumber;

    @NotNull(message = "Password must be required")
    private String password;

    private String roleName;


}
