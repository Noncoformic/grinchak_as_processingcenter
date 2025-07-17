package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.TransactionTypeDto;
import ru.edme.model.TransactionType;

@Mapper(componentModel = "spring")
public interface TransactionTypeMapper {
    TransactionTypeDto toDto(TransactionType e);
    TransactionType toEntity(TransactionTypeDto d);
}
