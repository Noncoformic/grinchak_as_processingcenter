package ru.edme.dto;

import lombok.Data;

@Data
public class SalesPointDto {
    private Long id;
    private String posName;
    private String posAddress;
    private String posInn;
    private Long acquiringBankId;
}
