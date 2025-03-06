package ru.edme.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account") // Название таблицы в БД
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "account_number", nullable = false, unique = true)
  private String accountNumber;

  @Column(name = "balance", nullable = false)
  private BigDecimal balance;

  @Column(name = "currency_id", nullable = false)
  private Long currencyId;

  @Column(name = "issuing_bank_id", nullable = false)
  private Long issuingBankId;

  public AccountBuilder toBuilder() {
    return builder()
        .id(id)
        .accountNumber(accountNumber)
        .balance(balance)
        .currencyId(currencyId)
        .issuingBankId(issuingBankId);
  }
}
