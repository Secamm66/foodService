package ru.ershov.project.deliveryservice.mappers.ToDTODeliveriesMappers;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ershov.project.deliveryservice.dto.responseToGetDeliveries.RestaurantDTO;
import ru.ershov.project.deliveryservice.mappers.ToDTOMapper;
import ru.ershov.project.common.models.Restaurant;

@Mapper(componentModel = "spring")
public interface RestaurantDTOMapper extends ToDTOMapper<Restaurant, RestaurantDTO> {

    @Override
    @Mapping(source = "coordinates", target = "distance", qualifiedByName = "calculateDistance")
    RestaurantDTO toDTO(Restaurant entity, @Context String courierCoordinates);
}