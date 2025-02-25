package ru.edme.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Account {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private Long currencyId;
    private Long issuingBankId;

}
