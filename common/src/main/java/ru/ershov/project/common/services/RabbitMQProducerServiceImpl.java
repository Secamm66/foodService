package ru.ershov.project.common.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQProducerServiceImpl<T> implements RabbitMQProducerService<T> {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String exchange, T object, String routingKey) {
        rabbitTemplate.convertAndSend(exchange, routingKey, object);
    }

//    public void sendMessage(OrderStatusChangedEvent event) {
//        switch (event.getStatus()) {
//            case ACCEPTED:
//                sendToOrderExchange(event, RabbitMQConstants.NOTIFICATION_CUSTOMER_ROUTING_KEY);
//                break;
//            case PAID:
//                sendToOrderExchange(event, RabbitMQConstants.ORDER_PAID_ROUTING_KEY, RabbitMQConstants.NOTIFICATION_CUSTOMER_ROUTING_KEY);
//                break;
//            case DENIED:
//                sendToKitchenExchange(event, RabbitMQConstants.ORDER_DENIED_ROUTING_KEY, RabbitMQConstants.NOTIFICATION_CUSTOMER_ROUTING_KEY);
//                break;
//            case ACTIVE:
//                sendToKitchenExchange(event, RabbitMQConstants.NOTIFICATION_CUSTOMER_ROUTING_KEY);
//                break;
//            case COMPLETE:
//                sendToKitchenExchange(event, RabbitMQConstants.KITCHEN_COMPLETE_ROUTING_KEY, RabbitMQConstants.NOTIFICATION_CUSTOMER_ROUTING_KEY);
//                break;
//            case ACCEPTED_FOR_DELIVERY:
//                sendToDeliveryExchange(event, RabbitMQConstants.DELIVERY_FIND_COURIERS_ROUTING_KEY);
//                break;
//            case DELIVERED:
//            case RECEIVED:
//                sendToDeliveryExchange(event, RabbitMQConstants.NOTIFICATION_CUSTOMER_ROUTING_KEY);
//                break;
//            default:
//                throw new InvalidStatusException("Invalid status in OrderStatusChangedEvent");
//        }
//    }
//
//    private void sendToOrderExchange(OrderStatusChangedEvent event, String... routingKeys) {
//        sendMessageToExchange(RabbitMQConstants.ORDER_STATUS_EXCHANGE, event, routingKeys);
//    }
//
//    private void sendToKitchenExchange(OrderStatusChangedEvent event, String... routingKeys) {
//        sendMessageToExchange(RabbitMQConstants.KITCHEN_EXCHANGE, event, routingKeys);
//    }
//
//    private void sendToDeliveryExchange(OrderStatusChangedEvent event, String... routingKeys) {
//        sendMessageToExchange(RabbitMQConstants.DELIVERY_EXCHANGE, event, routingKeys);
//    }
//
//    private void sendMessageToExchange(String exchange, OrderStatusChangedEvent event, String... routingKeys) {
//        for (String routingKey : routingKeys) {
//            rabbitTemplate.convertAndSend(exchange, routingKey, event);
//            log.info("Message has been sent to {} with routing key: {}", exchange, routingKey);
//        }
//    }
}