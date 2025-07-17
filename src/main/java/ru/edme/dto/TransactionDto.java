package ru.edme.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private Long id;
    private LocalDateTime transactionDate;
    private BigDecimal sum;
    private String transactionName;
    private Long accountId;
    private Long transactionTypeId;
    private Long cardId;
    private Long terminalId;
    private Long responseCodeId;
    private String authorizationCode;
}