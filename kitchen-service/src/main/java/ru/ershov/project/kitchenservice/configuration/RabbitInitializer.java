package ru.ershov.project.kitchenservice.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final AmqpAdmin amqpAdmin;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
            amqpAdmin.initialize();
            System.out.println("RabbitAdmin initialized!");
    }
}