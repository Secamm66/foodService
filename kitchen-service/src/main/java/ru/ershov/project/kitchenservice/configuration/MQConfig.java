package ru.ershov.project.kitchenservice.configuration;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ru.ershov.project.common.constants.RabbitMQConstants.*;

@Configuration
public class MQConfig {

    @Bean
    public Declarables kitchenServiceQueues() {
        Queue kitchenToOrderQueue = new Queue(KITCHEN_TO_ORDER_QUEUE, false);
        Queue kitchenToDeliveryQueue = new Queue(KITCHEN_TO_DELIVERY_QUEUE, false);
        Queue kitchenToNotificationQueue = new Queue(KITCHEN_TO_NOTIFICATION_QUEUE, false);
        TopicExchange orderStatusExchange = new TopicExchange(ORDER_STATUS_EXCHANGE);

        return new Declarables(kitchenToOrderQueue, kitchenToDeliveryQueue, kitchenToNotificationQueue, orderStatusExchange,
                BindingBuilder.bind(kitchenToOrderQueue).to(orderStatusExchange).with(ORDER_STATUS_DENIED_ROUTING_KEY),
                BindingBuilder.bind(kitchenToDeliveryQueue).to(orderStatusExchange).with(ORDER_STATUS_COMPLETE_ROUTING_KEY),
                BindingBuilder.bind(kitchenToNotificationQueue).to(orderStatusExchange).with(ORDER_STATUS__ROUTING_KEY));
    }
}