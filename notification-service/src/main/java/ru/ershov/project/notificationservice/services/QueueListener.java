package ru.ershov.project.notificationservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.ershov.project.common.dto.OrderStatusChangedEvent;

import static ru.ershov.project.common.constants.RabbitMQConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = ORDER_TO_NOTIFICATION_QUEUE)
    public void processOrderToNotificationQueue(OrderStatusChangedEvent event) {
        log.info("Received event from OrderService: {}", event);
        notificationService.processNotification(event);
    }

    @RabbitListener(queues = KITCHEN_TO_NOTIFICATION_QUEUE)
    public void processKitchenToNotificationQueue(OrderStatusChangedEvent event) {
        log.info("Received event from KitchenService: {}", event);
        notificationService.processNotification(event);
    }

    @RabbitListener(queues = DELIVERY_TO_NOTIFICATION_QUEUE)
    public void processDeliveryToNotificationQueue(OrderStatusChangedEvent event) {
        log.info("Received event from DeliveryService: {}", event);
        notificationService.processNotification(event);
    }
}