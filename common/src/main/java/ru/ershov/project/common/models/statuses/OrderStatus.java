package ru.ershov.project.common.models.statuses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static ru.ershov.project.common.constants.RabbitMQConstants.*;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    ACCEPTED(ORDER_STATUS_ACCEPTED_PAID_ROUTING_KEY), // Заказ принят

    PAID(ORDER_STATUS_ACCEPTED_PAID_ROUTING_KEY), // Оплачен

    ACTIVE(ORDER_STATUS_ACTIVE_ROUTING_KEY), // Подтвержден, готовится

    DENIED(ORDER_STATUS_DENIED_ROUTING_KEY), // Отклонён кухней

    COMPLETE(ORDER_STATUS_COMPLETE_ROUTING_KEY), // Готов

    ACCEPTED_FOR_DELIVERY(DELIVERY_FIND_COURIERS_ROUTING_KEY), // Принят в доставку

    DELIVERED(DELIVERY_FIND_COURIERS_ROUTING_KEY), // Доставляется

    RECEIVED(DELIVERY_FIND_COURIERS_ROUTING_KEY); // Получен клиентом

    private final String routingKey;
}