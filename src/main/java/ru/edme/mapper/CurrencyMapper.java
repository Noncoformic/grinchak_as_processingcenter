package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.CurrencyDto;
import ru.edme.model.Currency;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyDto toDto(Currency entity);
    Currency toEntity(CurrencyDto dto);
}
