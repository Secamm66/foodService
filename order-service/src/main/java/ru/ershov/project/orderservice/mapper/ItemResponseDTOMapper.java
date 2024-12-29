package ru.ershov.project.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ershov.project.common.mappers.ToDTOMapper;
import ru.ershov.project.orderservice.dto.ItemResponseDTO;
import ru.ershov.project.common.models.OrderItem;


@Mapper(componentModel = "spring")
public interface ItemResponseDTOMapper extends ToDTOMapper<OrderItem, ItemResponseDTO> {

    @Mapping(source = "price", target = "cost")
    @Mapping(source = "restaurantMenuItem.name", target = "name")
    @Mapping(source = "restaurantMenuItem.imagePath", target = "imagePath")
    @Mapping(source = "restaurantMenuItem.description", target = "description")
    @Override
    ItemResponseDTO toDTO(OrderItem entity);
}