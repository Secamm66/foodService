package ru.ershov.project.deliveryservice.mappers.ToDTODeliveriesMappers;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ershov.project.deliveryservice.dto.responseToGetDeliveries.CustomerDTO;
import ru.ershov.project.deliveryservice.mappers.ToDTOMapper;
import ru.ershov.project.common.models.Customer;

@Mapper(componentModel = "spring")
public interface CustomerDTOMapper extends ToDTOMapper<Customer, CustomerDTO> {

    @Override
    @Mapping(source = "coordinates", target = "distance", qualifiedByName = "calculateDistance")
    CustomerDTO toDTO(Customer entity, @Context String courierCoordinates);
}