package ru.ershov.project.kitchenservice.configuration;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.stereotype.Component;

@Component
public class RabbitAdminInitializer {

    public RabbitAdminInitializer(AmqpAdmin rabbitAdmin) {
        rabbitAdmin.initialize();
        System.out.println("RabbitAdmin initialized in Kitchen!");
    }
}