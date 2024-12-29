package ru.ershov.project.notificationservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.ershov.project.common.dto.OrderStatusChangedEvent;
import ru.ershov.project.common.models.Order;

@Component
@Slf4j
public class NotificationProcessor {

    public void handleNotification(Order order, OrderStatusChangedEvent event) {
        switch (event.getStatus()) {
            case ACCEPTED ->
                    notifyCustomer(order.getCustomer().getId(), "Your order id=" + order.getId() + " has been accepted and is awaiting payment");
            case PAID -> {
                notifyCustomer(order.getCustomer().getId(), "Your order id=" + order.getId() + " has been paid.");
                notifyRestaurant(order.getRestaurant().getId(), "New order id=" + order.getId() + " received. (Details/Accept/Reject)");
            }
            case ACTIVE ->
                    notifyCustomer(order.getCustomer().getId(), "Your order id=" + order.getId() + " is being prepared.");
            case DENIED -> {
                notifyCustomer(order.getCustomer().getId(), "Your order id=" + order.getId() + " has been canceled by the restaurant. Refund is being processed.");
                notifyRestaurant(order.getRestaurant().getId(), "You canceled order id=" + order.getId() + ".");
            }
            case COMPLETE -> {
                notifyCustomer(order.getCustomer().getId(), "Your order id=" + order.getId() + " is ready.");
                notifyRestaurant(order.getRestaurant().getId(), "Order id=" + order.getId() + " is being handed over to delivery.");
            }
            case ACCEPTED_FOR_DELIVERY -> {
                notifyRestaurant(order.getRestaurant().getId(), "Order id=" + order.getId() + " has been accepted by the delivery service.");
                notifyCustomer(order.getCustomer().getId(), "Your order id=" + order.getId() + " has been accepted for delivery. Courier search is ongoing.");
                notifyCourier(event.getMessage(), "New delivery for order id=" + order.getId() + " (Details/Accept/Ignore)");
            }
            case DELIVERED -> {
                notifyCourier(order.getCourier().getId(), "You accepted order id=" + order.getId() + " for delivery.");
                notifyCustomer(order.getCustomer().getId(), "Your order id=" + order.getId() + " is on the way. The courier will arrive soon.");
            }
            case RECEIVED -> {
                notifyCourier(order.getCourier().getId(), "You delivered order id=" + order.getId() + ".");
                notifyCustomer(order.getCustomer().getId(), "Your order id=" + order.getId() + " has been delivered to you. Enjoy!");
            }
            default -> log.warn("Unknown status for order id={}.", order.getId());
        }
    }

    private void notifyCustomer(Long customerId, String message) {
        log.info("Notification for customer id={}: {}", customerId, message);
    }

    private void notifyRestaurant(Long restaurantId, String message) {
        log.info("Notification for restaurant id={}: {}", restaurantId, message);
    }

    private void notifyCourier(Long courierId, String message) {
        log.info("Notification for courier id={}: {}", courierId, message);
    }

    private void notifyCourier(String courierIds, String message) {
        log.info("Notification for courier ids={}: {}", courierIds, message);
    }
}