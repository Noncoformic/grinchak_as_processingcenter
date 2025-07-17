package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.IssuingBankDto;
import ru.edme.model.IssuingBank;

@Mapper(componentModel = "spring")
public interface IssuingBankMapper {
    IssuingBankDto toDto(IssuingBank entity);
    IssuingBank toEntity(IssuingBankDto dto);
}