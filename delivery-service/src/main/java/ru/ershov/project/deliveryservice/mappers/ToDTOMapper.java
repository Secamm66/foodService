package ru.ershov.project.deliveryservice.mappers;

import org.mapstruct.Context;
import org.mapstruct.Named;
import ru.ershov.project.deliveryservice.util.DistanceCalculator;

public interface ToDTOMapper<E, D> {

    D toDTO(E entity, @Context String courierCoordinates);

    @Named("calculateDistance")
    default Double calculateDistance(String entityCoordinates, @Context String courierCoordinates) {
        return DistanceCalculator.calculateDistance(entityCoordinates, courierCoordinates);
    }
}