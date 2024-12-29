package ru.ershov.project.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ershov.project.common.mappers.ToDTOMapper;
import ru.ershov.project.orderservice.dto.OrderDTOGetResponse;
import ru.ershov.project.common.models.Order;

@Mapper(componentModel = "spring", uses = {ItemResponseDTOMapper.class})
public interface OrderResponseDTOMapper extends ToDTOMapper<Order, OrderDTOGetResponse> {

    @Mapping(source = "orderItems", target = "items")
    @Override
    OrderDTOGetResponse toDTO(Order entity);
}