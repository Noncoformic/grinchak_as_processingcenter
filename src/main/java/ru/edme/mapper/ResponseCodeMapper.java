package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.ResponseCodeDto;
import ru.edme.model.ResponseCode;

@Mapper(componentModel = "spring")
public interface ResponseCodeMapper {
    ResponseCodeDto toDto(ResponseCode e);
    ResponseCode toEntity(ResponseCodeDto d);
}