package ru.edme.dto;

import lombok.Data;

@Data
public class ResponseCodeDto {
    private Long id;
    private String errorCode;
    private String errorDescription;
    private String errorLevel;
}