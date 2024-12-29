package ru.ershov.project.deliveryservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ershov.project.common.dto.OrderActionRequestDTO;
import ru.ershov.project.common.dto.UpdatedStatusResponse;
import ru.ershov.project.deliveryservice.dto.responseToGetDeliveries.DeliveryListGetResponse;
import ru.ershov.project.deliveryservice.dto.responseToGetDeliveries.DeliveryResponseDTO;
import ru.ershov.project.deliveryservice.services.DeliveryService;

@Tag(name = "API для работы с доставками")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "Запрос на формирование списка доставок",
            description = "Возвращает список доставок в зависимости от статуса с пагинацией. " +
                    "Параметр deliveryStatus может принимать значения 'active' (доступные доставки) " +
                    "или 'complete' (завершённые доставки авторизованного курьера). " +
                    "Параметры page_index и page_count определяют страницу и количество доставок на странице")
    @GetMapping
    public ResponseEntity<DeliveryListGetResponse> getDeliveries(@RequestParam(name = "status", defaultValue = "active") String deliveryStatus,
                                                                 @RequestParam(name = "page_index", defaultValue = "0") int pageIndex,
                                                                 @RequestParam(name = "page_count", defaultValue = "10") int pageCount) {

        DeliveryListGetResponse response = deliveryService.getAllDeliveriesToSend(deliveryStatus, pageIndex, pageCount);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на формирование доставки по id",
            description = "Отображает детали новой поступившей доставки по её id")
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponseDTO> getDeliveryOrderDetails(@PathVariable("id") Long orderId) {
        DeliveryResponseDTO response = deliveryService.getDeliveryOrderDetails(orderId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на изменение статуса доставки",
            description = "Изменяет статус доставки по её id на 'ACCEPT' или 'COMPLETE' " +
                    "в зависимости от значения ключа orderAction в OrderActionRequestDTO. " +
                    "Возвращает id заказа с информационным сообщением")
    @PostMapping("/{id}")
    public ResponseEntity<UpdatedStatusResponse> processDelivery(@PathVariable("id") Long deliveryId,
                                                                 @RequestBody OrderActionRequestDTO dto) {
        UpdatedStatusResponse response = deliveryService.processDelivery(deliveryId, dto);
        return ResponseEntity.ok(response);
    }
}