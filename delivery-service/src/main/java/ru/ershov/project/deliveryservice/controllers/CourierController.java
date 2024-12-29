package ru.ershov.project.deliveryservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ershov.project.common.dto.StatusRequestDTO;
import ru.ershov.project.common.dto.UpdatedStatusResponse;
import ru.ershov.project.deliveryservice.services.CourierService;

@Tag(name = "API для работы с доставками")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveries/courier")
public class CourierController {

    private final CourierService courierService;

    @Operation(summary = "Запрос на изменение статуса курьера",
            description = "Изменяет статус курьера по его id на 'AVAILABLE' или 'NOT_AVAILABLE' " +
                    "в зависимости от значения ключа status в StatusRequestDTO. " +
                    "Возвращает id курьера с информационным сообщением")
    @PatchMapping("/{id}/status")
    public ResponseEntity<UpdatedStatusResponse> updateCourierStatus(@PathVariable("id") Long courierId,
                                                                     @RequestBody StatusRequestDTO dto) {
        UpdatedStatusResponse response = courierService.updateCourierStatus(courierId, dto);
        return ResponseEntity.ok(response);
    }
}