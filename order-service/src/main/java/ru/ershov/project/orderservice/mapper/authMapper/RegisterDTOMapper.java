package ru.ershov.project.orderservice.mapper.authMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ershov.project.common.mappers.ToEntityMapper;
import ru.ershov.project.common.models.Customer;
import ru.ershov.project.orderservice.dto.auth.RegisterDTO;

@Mapper(componentModel = "spring")
public interface RegisterDTOMapper extends ToEntityMapper<Customer, RegisterDTO> {

    @Mapping(source = "username", target = "user.username")
    @Mapping(source = "password", target = "user.password")
    @Override
    Customer toEntity(RegisterDTO dto);
}