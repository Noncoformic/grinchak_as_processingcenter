package ru.edme.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Transaction {
    private Long id;
    private Date transactionDate;
    private BigDecimal sum;
    private String transactionName;
    private Long accountId;
    private Long TransactionTypeId;
    private Long cardId;
    private Long terminalId;
    private Long responseCodeId;
    private String authorizationCode;
    private Date receivedFromIssuingBank;
    private Date sentToIssuingBank;

}
