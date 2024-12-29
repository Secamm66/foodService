package ru.ershov.project.deliveryservice.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.ershov.project.common.dto.OrderStatusChangedEvent;
import ru.ershov.project.common.services.RabbitMQProducerServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static ru.ershov.project.common.constants.RabbitMQConstants.KITCHEN_TO_DELIVERY_QUEUE;
import static ru.ershov.project.common.constants.RabbitMQConstants.ORDER_STATUS_EXCHANGE;
import static ru.ershov.project.common.models.statuses.OrderStatus.ACCEPTED_FOR_DELIVERY;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceQueueListener {

    private final DeliveryService deliveryService;
    private final CourierService courierService;
    private final RabbitMQProducerServiceImpl<OrderStatusChangedEvent> producerService;

    @SneakyThrows
    @RabbitListener(queues = KITCHEN_TO_DELIVERY_QUEUE)
    public void processMyQueue(OrderStatusChangedEvent event) {

        log.info(String.format("Received from KitchenToDeliveryQueue: %s", event));
        deliveryService.acceptOrderFromKitchen(event.getOrderId());
        List<Long> couriersList = courierService.getAvailableCouriers();
        event.setUpdatedAt(LocalDateTime.now())
                .setMessage(couriersList.toString())
                .setStatus(ACCEPTED_FOR_DELIVERY);

        producerService.sendMessage(ORDER_STATUS_EXCHANGE, event, ACCEPTED_FOR_DELIVERY.getRoutingKey());
        /*
            Заказ приходит с кухни, меняется статус на ACCEPTED_FOR_DELIVERY,
            загружается список id доступных курьеров и отправляется в
            нотификейшен для рассылки уведомлений этим курьерам
        */
    }
}