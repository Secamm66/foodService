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
}