package com.vvts.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserKycUpdateDto {

    private Integer userId;

    private String email;

    private String contact;

    @NotNull(message = "Address must be required")
    private String name;

    private String address;

    @NotNull(message = "Citizenship no must be required")
    private String citizenshipNo;

    @NotNull(message = "Profile Picture must be required")
    private MultipartFile profilePicture;

    @NotNull(message = "citizenship font image must be required")
    private MultipartFile citizenshipFont;

    @NotNull(message = "citizenship back image must be required")
    private MultipartFile citizenshipBack;

}
