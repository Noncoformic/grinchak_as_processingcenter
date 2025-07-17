package ru.edme.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.edme.dto.AccountDto;
import ru.edme.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "currency.id",      target = "currencyId")
    @Mapping(source = "issuingBank.id",   target = "issuingBankId")
    AccountDto toDto(Account entity);

    @Mapping(source = "currencyId",    target = "currency.id")
    @Mapping(source = "issuingBankId", target = "issuingBank.id")
    Account toEntity(AccountDto dto);
}
