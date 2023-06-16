package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther kul.paudel
 * @created at 2023-06-16
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NumberPlateDto {

    private String id;
    private String label;
    private int xmin;
    private int ymin;
    private int xmax;
    private int ymax;
    private double score;
    private String ocr_text;
    private String type;
    private String status;
    private int page_no;
    private String label_id;

}
