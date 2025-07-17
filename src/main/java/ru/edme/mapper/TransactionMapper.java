package ru.edme.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.edme.dto.TransactionDto;
import ru.edme.model.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source="account.id",           target="accountId")
    @Mapping(source="transactionType.id",   target="transactionTypeId")
    @Mapping(source="card.id",              target="cardId")
    @Mapping(source="terminal.id",          target="terminalId")
    @Mapping(source="responseCode.id",      target="responseCodeId")
    TransactionDto toDto(Transaction e);

    @Mapping(source="accountId",           target="account.id")
    @Mapping(source="transactionTypeId",   target="transactionType.id")
    @Mapping(source="cardId",              target="card.id")
    @Mapping(source="terminalId",          target="terminal.id")
    @Mapping(source="responseCodeId",      target="responseCode.id")
    Transaction toEntity(TransactionDto d);
}
