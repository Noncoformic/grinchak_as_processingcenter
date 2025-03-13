package ru.edme.service;

import org.springframework.stereotype.Service;
import ru.edme.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class CardBuilderService {

    public Card buildCard(String cardNumber, LocalDate expirationDate, String holderName,
                          CardStatus cardStatus, PaymentSystem paymentSystem,
                          Account account, LocalDateTime receivedFromIssuingBank,
                          LocalDateTime sentToIssuingBank) {
        return Card.builder()
                .cardNumber(cardNumber)
                .expirationDate(expirationDate)
                .holderName(holderName)
                .cardStatus(cardStatus)
                .paymentSystem(paymentSystem)
                .account(account)
                .receivedFromIssuingBank(receivedFromIssuingBank)
                .sentToIssuingBank(sentToIssuingBank)
                .build();
    }
}
