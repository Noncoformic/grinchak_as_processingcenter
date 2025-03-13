package ru.edme.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "sum", nullable = false, precision = 19, scale = 2)
    private BigDecimal sum;

    @Column(name = "transaction_name", nullable = false, length = 255)
    private String transactionName;

    // Связь с Account
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Связь с TransactionType
    @ManyToOne
    @JoinColumn(name = "transaction_type_id", nullable = false)
    private TransactionType transactionType;

    // Связь с Card
    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    // Связь с Terminal
    @ManyToOne
    @JoinColumn(name = "terminal_id")
    private Terminal terminal;

    // Связь с ResponseCode
    @ManyToOne
    @JoinColumn(name = "response_code_id")
    private ResponseCode responseCode;

    @Column(name = "authorization_code", length = 6)
    private String authorizationCode;

    @Column(name = "received_from_issuing_bank")
    private LocalDateTime receivedFromIssuingBank;

    @Column(name = "sent_to_issuing_bank")
    private LocalDateTime sentToIssuingBank;
}
