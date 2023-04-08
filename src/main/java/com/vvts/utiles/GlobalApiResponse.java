package com.vvts.utiles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalApiResponse {

    private String message ;

    private Boolean status;

    private Object data;

}
