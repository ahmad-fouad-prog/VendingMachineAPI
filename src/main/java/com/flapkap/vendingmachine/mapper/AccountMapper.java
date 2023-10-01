package com.flapkap.vendingmachine.mapper;

import com.flapkap.vendingmachine.dto.AccountDTO;
import com.flapkap.vendingmachine.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "userId", ignore = true)
    AccountDTO toDTO(Account account);

    @Mapping(target = "id", ignore = true)
    Account toEntity(AccountDTO accountDTO);
}
