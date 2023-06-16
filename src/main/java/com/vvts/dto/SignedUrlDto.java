package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther kul.paudel
 * @created at 2023-06-15
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignedUrlDto {

    private String original;
    private String original_compressed;
    private String thumbnail;
    private String acw_rotate_90;
    private String acw_rotate_180;
    private String acw_rotate_270;
    private String original_with_long_expiry;
}
