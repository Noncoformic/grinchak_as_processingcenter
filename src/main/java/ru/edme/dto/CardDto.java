package ru.edme.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CardDto {

    private Long id;

    @NotBlank
    @Size(max = 50)
    private String cardNumber;

    @NotNull
    private LocalDate expirationDate;

    @NotBlank
    @Size(max = 50)
    private String holderName;

    /** ID статуса карты (ACTIVE, BLOCKED …) */
    @NotNull
    private Long cardStatusId;

    /** ID платёжной системы (VISA, MASTERCARD …) */
    @NotNull
    private Long paymentSystemId;

    /** ID счёта владельца */
    @NotNull
    private Long accountId;

    private LocalDateTime receivedFromIssuingBank;
    private LocalDateTime sentToIssuingBank;
}

