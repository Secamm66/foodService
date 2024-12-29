package ru.ershov.project.kitchenservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ershov.project.common.dto.UpdatedStatusResponse;
import ru.ershov.project.common.util.exception.EntityNotFoundException;
import ru.ershov.project.common.dto.StatusRequestDTO;
import ru.ershov.project.common.models.Restaurant;
import ru.ershov.project.common.repositories.RestaurantRepository;
import ru.ershov.project.kitchenservice.util.RestaurantParametersValidator;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final Long restaurantId = 1L; // TODO: Будет браться из Authentication

    @Transactional
    public UpdatedStatusResponse updateRestaurantStatus(StatusRequestDTO dto) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        RestaurantParametersValidator.checkRestaurantStatusParametersBeforeUpdate(restaurant, dto);
        RestaurantParametersValidator.convertToOrderStatusParametersForUpdate(restaurant, dto);
        restaurantRepository.save(restaurant);
        return new UpdatedStatusResponse()
                .setId(restaurantId)
                .setMessage("Restaurant is " + dto.getStatus());
    }

    private Restaurant getRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant with id=" + restaurantId + " not found"));
    }
}