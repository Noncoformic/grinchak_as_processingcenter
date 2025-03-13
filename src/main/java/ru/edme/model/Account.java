package ru.edme.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Data
@Builder(toBuilder = true) // Автоматически создаёт `toBuilder()`
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "account_number", nullable = false, unique = true, length = 20)
  private String accountNumber;

  @Column(name = "balance", nullable = false, precision = 19, scale = 2)
  private BigDecimal balance;

  // Связь с `Currency`
  @ManyToOne
  @JoinColumn(name = "currency_id", nullable = false)
  private Currency currency;

  // Связь с `IssuingBank`
  @ManyToOne
  @JoinColumn(name = "issuing_bank_id", nullable = false)
  private IssuingBank issuingBank;
}

