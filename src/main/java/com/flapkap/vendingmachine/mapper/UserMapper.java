package com.flapkap.vendingmachine.mapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flapkap.vendingmachine.dto.UserDTO;
import com.flapkap.vendingmachine.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target ="password", ignore=true)
    UserDTO toDTO(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO userDTO);
}