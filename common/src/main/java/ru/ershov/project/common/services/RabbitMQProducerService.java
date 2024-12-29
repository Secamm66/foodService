package ru.ershov.project.common.services;

public interface RabbitMQProducerService<T> {

    void sendMessage(String exchange, T object, String routingKey);

}