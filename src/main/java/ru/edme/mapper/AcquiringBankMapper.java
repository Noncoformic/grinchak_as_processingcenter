package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.AcquiringBankDto;
import ru.edme.model.AcquiringBank;

@Mapper(componentModel = "spring")
public interface AcquiringBankMapper {
    AcquiringBankDto toDto(AcquiringBank entity);
    AcquiringBank toEntity(AcquiringBankDto dto);
}