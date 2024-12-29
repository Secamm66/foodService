package ru.ershov.project.orderservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ershov.project.common.dto.UpdatedStatusResponse;
import ru.ershov.project.orderservice.dto.*;
import ru.ershov.project.orderservice.services.OrderService;

import javax.validation.Valid;

@Tag(name = "API для работы с заказами")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Запрос на формирование истории заказов клиента",
            description = "Возвращает список заказов авторизованного клиента с пагинацией. " +
                    "Параметры page_index и page_count определяют номер страницы и количество заказов")
    @GetMapping
    public ResponseEntity<OrderListGetResponse> getAllOrders(@RequestParam(name = "page_index", defaultValue = "0") int pageIndex,
                                                             @RequestParam(name = "page_count", defaultValue = "10") int pageCount) {
        OrderListGetResponse orderListGetResponse = orderService.getAllOrdersToSend(pageIndex, pageCount);
        return ResponseEntity.ok(orderListGetResponse);
    }

    @Operation(summary = "Запрос на формирование заказа по id",
            description = "Возвращает информацию о заказе авторизованного клиента по его id")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTOGetResponse> getOrder(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderService.getOrderByIdToSend(orderId));
    }

    @Operation(summary = "Запрос на создание нового заказа авторизованным клиентом",
            description = "Создает новый заказ на основе принимаемого ДТО OrderRequestDTO. " +
                    "Возвращает id заказа с ссылкой на оплату")
    @PostMapping()
    public ResponseEntity<OrderCreatePostResponse> createOrder(@RequestBody @Valid OrderRequestDTO dto, BindingResult bindingResult) {
        OrderCreatePostResponse orderCreatePostResponse = orderService.createOrder(dto, bindingResult);
        return ResponseEntity.ok(orderCreatePostResponse);
    }

    @Operation(summary = "Запрос на изменение статуса заказа на 'PAID'",
            description = "Изменяет статус заказа на 'PAID' по его id. Возвращает id заказа с сообщением. " +
                    "Должен выполняться автоматически после подтверждения оплаты сервисом платежей")
    @PatchMapping("/{id}/status")
    public ResponseEntity<UpdatedStatusResponse> updateOrderStatus(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderService.updateOrderStatusToPaid(orderId));
    }
}