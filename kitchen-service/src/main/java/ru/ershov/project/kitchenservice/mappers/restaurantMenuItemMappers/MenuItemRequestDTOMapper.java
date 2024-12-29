package ru.ershov.project.kitchenservice.mappers.restaurantMenuItemMappers;

import org.mapstruct.Mapper;
import ru.ershov.project.common.mappers.ToEntityMapper;
import ru.ershov.project.kitchenservice.dto.restaurantMenuItem.MenuItemRequestDTO;
import ru.ershov.project.common.models.RestaurantMenuItem;

@Mapper(componentModel = "spring")
public interface MenuItemRequestDTOMapper extends ToEntityMapper<RestaurantMenuItem, MenuItemRequestDTO> {

}