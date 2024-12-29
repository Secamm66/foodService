package ru.ershov.project.kitchenservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ershov.project.common.dto.StatusRequestDTO;
import ru.ershov.project.common.dto.UpdatedStatusResponse;
import ru.ershov.project.kitchenservice.services.RestaurantService;

@Tag(name = "API для работы с ресторанами")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/restaurants/")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Operation(summary = "Запрос на изменение статуса ресторана",
            description = "Изменяет статус ресторана на 'OPEN' или 'CLOSED' в зависимости от значения " +
                    "ключа status в StatusRequestDTO. Возвращает id ресторана с информационным сообщением")
    @PatchMapping("/status")
    public ResponseEntity<UpdatedStatusResponse> updateRestaurantStatus(@RequestBody StatusRequestDTO dto) {
        UpdatedStatusResponse response = restaurantService.updateRestaurantStatus(dto);
        return ResponseEntity.ok(response);
    }
}