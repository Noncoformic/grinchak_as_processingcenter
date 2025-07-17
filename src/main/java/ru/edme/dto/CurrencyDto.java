package ru.edme.dto;

import lombok.Data;

@Data
public class CurrencyDto {
    private Long id;
    private String currencyDigitalCode;   // 643
    private String currencyLetterCode;    // RUB
    private String currencyName;          // Российский рубль
}
