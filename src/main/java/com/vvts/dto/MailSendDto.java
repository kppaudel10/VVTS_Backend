package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther kul.paudel
 * @created at 2023-05-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailSendDto {
    private String userName;
    private String email;
    private String message;
    private String pinCode;

}
