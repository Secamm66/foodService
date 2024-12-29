package ru.ershov.project.kitchenservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ershov.project.common.dto.DetailsOfNewOrderDTO;
import ru.ershov.project.common.dto.OrderActionRequestDTO;
import ru.ershov.project.kitchenservice.dto.OrderListGetResponse;
import ru.ershov.project.common.dto.UpdatedStatusResponse;
import ru.ershov.project.kitchenservice.services.OrderService;

import java.util.List;

@Tag(name = "API для работы с ресторанами")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/restaurants/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Запрос на формирование списка заказов ресторана",
            description = "Возвращает список заказов в зависимости от его статуса с пагинацией. " +
                    "Параметр status может принимать одно или несколько значений: denied, active, complete. " +
                    "Параметры page_index и page_count определяют страницу и количество заказов на странице")
    @GetMapping()
    public ResponseEntity<OrderListGetResponse> getAllOrders(@RequestParam(name = "status", defaultValue = "active") List<String> orderStatuses,
                                                             @RequestParam(name = "page_index", defaultValue = "0") int pageIndex,
                                                             @RequestParam(name = "page_count", defaultValue = "10") int pageCount) {
        OrderListGetResponse orderResponse = orderService.getAllOrdersToSend(orderStatuses, pageIndex, pageCount);
        return ResponseEntity.ok(orderResponse);
    }

    @Operation(summary = "Запрос на формирование заказа по id",
            description = "Отображает детали нового поступившего заказа по его id")
    @GetMapping("/{id}")
    public ResponseEntity<DetailsOfNewOrderDTO> getOrderById(@PathVariable("id") Long orderId) {
        DetailsOfNewOrderDTO details = orderService.getDetailsOfNewOrderToSend(orderId);
        return ResponseEntity.ok(details);
    }

    @Operation(summary = "Запрос на изменение статуса заказа",
            description = "Изменяет статус заказа по его id на 'DENIED', 'ACTIVE' или 'COMPLETE' " +
                    "в зависимости от значения ключа orderAction в OrderActionRequestDTO. " +
                    "Возвращает id заказа с информационным сообщением")
    @PatchMapping("/{id}/status")
    public ResponseEntity<UpdatedStatusResponse> processOrderAction(@PathVariable("id") Long orderId,
                                                                    @RequestBody OrderActionRequestDTO dto) {
        UpdatedStatusResponse response = orderService.processOrderAction(orderId, dto);
        return ResponseEntity.ok(response);
    }
}