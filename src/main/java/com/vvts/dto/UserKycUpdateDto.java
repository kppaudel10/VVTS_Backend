package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserKycUpdateDto {

    private Integer userId;

    @NotNull(message = "Citizenship no must be required")
    private String citizenshipNo;

    @NotNull(message = "Profile Picture must be required")
    private MultipartFile profilePicture;

    @NotNull(message = "citizenship font image must be required")
    private MultipartFile citizenshipFont;

    @NotNull(message = "citizenship back image must be required")
    private MultipartFile citizenshipBack;

}
