package ru.ershov.project.orderservice.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.ershov.project.common.constants.RabbitMQConstants;
import ru.ershov.project.common.dto.OrderStatusChangedEvent;

@Service
@Slf4j
@RequiredArgsConstructor
public class QueueListener {

    @SneakyThrows
    @RabbitListener(queues = RabbitMQConstants.KITCHEN_TO_ORDER_QUEUE)
    public void processMyQueue(OrderStatusChangedEvent event) {
        log.info(String.format("Received from KitchenToOrderQueue: %s", event.toString()));

        /*
           Добавляем сюда бизнес-логику, т.е. что сделать, если заказ отклонён кухней
           Для примера удаляем этот заказ из БД

         */
    }
}