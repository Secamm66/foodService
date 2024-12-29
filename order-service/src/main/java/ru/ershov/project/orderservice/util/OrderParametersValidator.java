package ru.ershov.project.orderservice.util;

import ru.ershov.project.common.models.Order;
import ru.ershov.project.common.models.statuses.OrderStatus;
import ru.ershov.project.common.util.exception.InvalidStatusException;

public class OrderParametersValidator {

    public static void checkOrderStatusBeforeUpdate(Order orderToUpdate) {
        if (!orderToUpdate.getStatus().equals(OrderStatus.ACCEPTED)) {
            throw new InvalidStatusException("Incorrect status with orderId= " + orderToUpdate.getId());
        }
    }
}