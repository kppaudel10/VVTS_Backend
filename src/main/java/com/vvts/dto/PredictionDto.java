package com.vvts.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import global.PredictionDtoDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @auther kul.paudel
 * @created at 2023-06-15
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder

//@JsonDeserialize(using = PredictionDtoDeserializer.class)
public class PredictionDto {

    private String message;
    private PredictionResultDto[] result;
    private Map<String, SignedUrlDto> signed_urls;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PredictionResultDto[] getResult() {
        return result;
    }

    public void setResult(PredictionResultDto[] result) {
        this.result = result;
    }

    public Map<String, SignedUrlDto> getSigned_urls() {
        return signed_urls;
    }

    public void setSigned_urls(Map<String, SignedUrlDto> signed_urls) {
        this.signed_urls = signed_urls;
    }
}
