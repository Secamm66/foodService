package ru.ershov.project.kitchenservice.util;

import ru.ershov.project.common.models.Restaurant;
import ru.ershov.project.common.models.statuses.RestaurantStatus;
import ru.ershov.project.common.util.exception.InvalidStatusException;
import ru.ershov.project.common.dto.StatusRequestDTO;

public class RestaurantParametersValidator {

    public static void checkRestaurantStatusParametersBeforeUpdate(Restaurant restaurant, StatusRequestDTO dto) {
        switch (restaurant.getStatus()) {
            case OPEN -> {
                if (dto.getStatus().equalsIgnoreCase("open")) {
                    throw new InvalidStatusException("Restaurant is already open");
                }
            }
            case CLOSED -> {
                if (dto.getStatus().equalsIgnoreCase("closed")) {
                    throw new InvalidStatusException("Restaurant is already closed");
                }
            }
            default -> throw new InvalidStatusException("Invalid restaurant status");
        }
    }

    public static void convertToOrderStatusParametersForUpdate(Restaurant restaurant, StatusRequestDTO dto) {
        switch (dto.getStatus().toLowerCase()) {
            case "open" -> restaurant.setStatus(RestaurantStatus.OPEN);
            case "closed" -> restaurant.setStatus(RestaurantStatus.CLOSED);
            default -> throw new InvalidStatusException("restaurantStatus is not valid");
        }
    }
}