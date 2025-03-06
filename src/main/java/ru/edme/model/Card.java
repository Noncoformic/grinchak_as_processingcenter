package ru.edme.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "card")
public class Card {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "card_number", nullable = false, length = 50)
  private String cardNumber;

  @Column(name = "expiration_date", nullable = false)
  private LocalDate expirationDate;

  @Column(name = "holder_name", nullable = false, length = 50)
  private String holderName;

  @Column(name = "card_status_id", nullable = false)
  private Long cardStatusId;

  @Column(name = "payment_system_id", nullable = false)
  private Long paymentSystemId;

  @Column(name = "account_id", nullable = false)
  private Long accountId;

  @Column(name = "received_from_issuing_bank", nullable = true)
  private LocalDateTime receivedFromIssuingBank;

  @Column(name = "sent_to_issuing_bank", nullable = true)
  private LocalDateTime sentToIssuingBank;

  public CardBuilder toBuilder(){

      return Card.builder()
              .id(id)
              .cardNumber(cardNumber)
              .expirationDate(expirationDate)
              .holderName(holderName)
              .cardStatusId(cardStatusId)
              .paymentSystemId(paymentSystemId)
              .accountId(accountId)
              .receivedFromIssuingBank(receivedFromIssuingBank)
              .sentToIssuingBank(sentToIssuingBank);

  }


}

