package ru.ershov.project.orderservice.configuration;

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
    public Declarables orderServiceQueues() {
        Queue orderToNotificationQueue = new Queue(ORDER_TO_NOTIFICATION_QUEUE, false);
        TopicExchange orderStatusExchange = new TopicExchange(ORDER_STATUS_EXCHANGE);

        return new Declarables(
                orderToNotificationQueue, orderStatusExchange,
                BindingBuilder.bind(orderToNotificationQueue).to(orderStatusExchange).with(ORDER_STATUS_ACCEPTED_PAID_ROUTING_KEY)
        );
    }
}