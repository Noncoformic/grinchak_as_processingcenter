package ru.edme.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
  private Long id;
  private String cardNumber;
  private Date expirationDate;
  private String holderName;
  private Long cardStatusId;
  private Long paymentSystemId;
  private Long accountId;



 

    
}
