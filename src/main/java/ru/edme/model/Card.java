package ru.edme.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "card")
public class Card {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "card_number", nullable = false, length = 50, unique = true)
  private String cardNumber;

  @Column(name = "expiration_date", nullable = false)
  private LocalDate expirationDate;

  @Column(name = "holder_name", nullable = false, length = 50)
  private String holderName;

  // Связь с `CardStatus`
  @ManyToOne
  @JoinColumn(name = "card_status_id", nullable = false)
  private CardStatus cardStatus;

  // Связь с `PaymentSystem`
  @ManyToOne
  @JoinColumn(name = "payment_system_id", nullable = false)
  private PaymentSystem paymentSystem;

  // Связь с `Account`
  @ManyToOne
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  @Column(name = "received_from_issuing_bank")
  private LocalDateTime receivedFromIssuingBank;

  @Column(name = "sent_to_issuing_bank")
  private LocalDateTime sentToIssuingBank;
}
