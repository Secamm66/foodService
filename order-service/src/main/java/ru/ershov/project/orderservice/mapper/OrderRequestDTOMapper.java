package ru.ershov.project.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ershov.project.common.mappers.ToEntityMapper;
import ru.ershov.project.orderservice.dto.OrderRequestDTO;
import ru.ershov.project.common.models.Order;

@Mapper(componentModel = "spring", uses = {MenuItemsRequestDTOMapper.class})
public interface OrderRequestDTOMapper extends ToEntityMapper<Order, OrderRequestDTO> {

    @Mapping(source = "menuItems", target = "orderItems")
    @Mapping(source = "restaurantId", target = "restaurant.id")
    @Override
    Order toEntity(OrderRequestDTO dto);
}