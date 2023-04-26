package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther kul.paudel
 * @created at 2023-04-26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KycUpdateResponseDto {

    private Integer userId;

    private String citizenshipNo;

    private String name;

    private String profilePictureUrl;

    private String citizenshipFontUrl;

    private String citizenshipBackUrl;

}
