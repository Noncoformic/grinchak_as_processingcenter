package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.CardStatusDto;
import ru.edme.model.CardStatus;

@Mapper(componentModel = "spring")
public interface CardStatusMapper {
    CardStatusDto toDto(CardStatus entity);
    CardStatus toEntity(CardStatusDto dto);
}