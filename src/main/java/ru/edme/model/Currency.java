package ru.edme.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Currency {
    private Long id;
    private String currencyDigitalCode;
    private String currencyLetterCode;
    private String currencyName;
}
