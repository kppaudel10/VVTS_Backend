package com.vvts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @auther kul.paudel
 * @created at 2023-05-28
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellRequestActionPojo {

    @NotNull(message = "Action type must not be null")
    @NotEmpty(message = "Action type must not be empty")
    private String actionType;

    @NotNull(message = "ID must not be null")
    private Integer id;

    @NotNull(message = "ActionBy must not be null")
    @NotEmpty(message = "ActionBy must not be empty")
    private String actionBy;

}
