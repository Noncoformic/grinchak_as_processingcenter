package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.SalesPointDto;
import ru.edme.model.SalesPoint;

@Mapper(componentModel = "spring")
public interface SalesPointMapper {
    SalesPointDto toDto(SalesPoint e);

    SalesPoint toEntity(SalesPointDto d);
}
