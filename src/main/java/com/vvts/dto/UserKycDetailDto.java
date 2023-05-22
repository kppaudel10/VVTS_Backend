package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

/**
 * @auther kul.paudel
 * @created at 2023-05-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserKycDetailDto {

    Integer userId;
    String name;
    String address;
    String email;
    String contact;
    String citizenshipNo;
    Resource profilePictureUrl;
    Resource citizenshipFontUrl;
    Resource citizenshipBackUrl;

}
