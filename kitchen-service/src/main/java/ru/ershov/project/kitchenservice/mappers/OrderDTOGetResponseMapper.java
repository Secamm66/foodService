package ru.ershov.project.kitchenservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ershov.project.common.mappers.ToDTOMapper;
import ru.ershov.project.kitchenservice.dto.OrderDTOGetResponse;
import ru.ershov.project.common.models.Order;

@Mapper(componentModel = "spring", uses = MenuItemsResponseDTOMapper.class)
public interface OrderDTOGetResponseMapper extends ToDTOMapper<Order, OrderDTOGetResponse> {

    @Override
    @Mapping(source = "orderItems", target = "menuItems")
    OrderDTOGetResponse toDTO(Order order);
}