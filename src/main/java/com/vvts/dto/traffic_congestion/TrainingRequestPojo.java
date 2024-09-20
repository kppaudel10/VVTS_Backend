package com.vvts.dto.traffic_congestion;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TrainingRequestPojo {

    @NotNull(message = "K value must be required")
    private Integer kValue;

    @NotNull(message = "Training Data must be required")
    private MultipartFile trainingDataFile;
}
