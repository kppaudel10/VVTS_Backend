package com.vvts.dto;

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
public class PublicUserBasicDataDto {

    private Integer id;

    private String name;

    private String email;

    private String mobileNumber;

    private String password;

}
