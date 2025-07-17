package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.MerchantCategoryCodeDto;
import ru.edme.model.MerchantCategoryCode;

@Mapper(componentModel = "spring")
public interface MerchantCategoryCodeMapper {
    MerchantCategoryCodeDto toDto(MerchantCategoryCode e);
    MerchantCategoryCode toEntity(MerchantCategoryCodeDto d);
}
