package ru.edme.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.edme.dto.CardDto;
import ru.edme.model.Card;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(source = "cardStatus.id",   target = "cardStatusId")
    @Mapping(source = "paymentSystem.id",target = "paymentSystemId")
    @Mapping(source = "account.id",      target = "accountId")
    CardDto toDto(Card entity);

    @Mapping(source = "cardStatusId",    target = "cardStatus.id")
    @Mapping(source = "paymentSystemId", target = "paymentSystem.id")
    @Mapping(source = "accountId",       target = "account.id")
    Card toEntity(CardDto dto);
}
