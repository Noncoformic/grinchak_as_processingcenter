package ru.edme.dto;

import lombok.Data;

@Data
public class IssuingBankDto {
    private Long id;
    private String bic;
    private String abbreviatedName;
}