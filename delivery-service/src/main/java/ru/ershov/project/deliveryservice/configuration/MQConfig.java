package ru.ershov.project.deliveryservice.configuration;

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
        Queue deliveryToNotificationQueue = new Queue(DELIVERY_TO_NOTIFICATION_QUEUE, false);
        TopicExchange orderStatusExchange = new TopicExchange(ORDER_STATUS_EXCHANGE);

        return new Declarables(deliveryToNotificationQueue, orderStatusExchange,
                BindingBuilder.bind(deliveryToNotificationQueue).to(orderStatusExchange).with(DELIVERY_FIND_COURIERS_ROUTING_KEY));
    }
}