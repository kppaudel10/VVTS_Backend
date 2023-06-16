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
public class PredictionResultDto {

    private String message;
    private String input;
    private NumberPlateDto[] prediction;
    private int page;
    private String request_file_id;
    private String filepath;
    private String id;
    private int rotation;
    private String file_url;
    private String request_metadata;
    private String processing_type;


}
