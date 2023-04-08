package com.vvts.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicUserDto {

    private Integer id;

    private String name;

    private String address;

    private String email;

    private String mobileNumber;

    private String password;

    private Boolean isEnable;

}
