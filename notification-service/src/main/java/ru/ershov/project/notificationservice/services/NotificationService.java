package ru.ershov.project.notificationservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ershov.project.common.dto.OrderStatusChangedEvent;
import ru.ershov.project.common.repositories.OrderRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationProcessor notificationProcessor;
    private final OrderRepository orderRepository;

    public void processNotification(OrderStatusChangedEvent event) {
        orderRepository.findById(event.getOrderId())
                .ifPresentOrElse(foundOrder -> notificationProcessor.handleNotification(foundOrder, event),
                        () -> log.warn("Order with id={}: not found", event.getOrderId()));
    }
}