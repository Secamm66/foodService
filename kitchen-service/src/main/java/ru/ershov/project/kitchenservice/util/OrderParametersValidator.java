package ru.ershov.project.kitchenservice.util;

import ru.ershov.project.common.models.Order;
import ru.ershov.project.common.models.statuses.OrderStatus;
import ru.ershov.project.common.util.exception.InvalidPageParameterException;
import ru.ershov.project.common.util.exception.InvalidStatusException;

import java.util.ArrayList;
import java.util.List;

public class OrderParametersValidator {

    public static List<OrderStatus> checkAndConvertOrderStatusParametersForFiltration(List<String> orderStatuses) {
        List<OrderStatus> orderStatusesList = new ArrayList<>();

        for (String orderStatus : orderStatuses) {
            switch (orderStatus.toLowerCase()) {
                case "active" -> orderStatusesList.add(OrderStatus.ACTIVE);
                case "complete" -> {
                    orderStatusesList.add(OrderStatus.COMPLETE);
                    orderStatusesList.add(OrderStatus.ACCEPTED_FOR_DELIVERY);
                    orderStatusesList.add(OrderStatus.DELIVERED);
                    orderStatusesList.add(OrderStatus.RECEIVED);
                }
                case "denied" -> orderStatusesList.add(OrderStatus.DENIED);
                default -> throw new InvalidPageParameterException("Invalid status parameters");
            }
        }
        return orderStatusesList;
    }

    public static void checkOrderActionBeforeUpdate(Order orderToUpdate, String orderAction) {
        OrderStatus currentStatus = orderToUpdate.getStatus();

        switch (currentStatus) {
            case PAID -> {
                if (orderAction.equalsIgnoreCase("complete")) {
                    throw new InvalidPageParameterException("Order with status PAID cannot be set to COMPLETE");
                }
            }
            case ACTIVE -> {
                if (orderAction.equalsIgnoreCase("active")) {
                    throw new InvalidPageParameterException("Order is already ACTIVE");
                }
                if (orderAction.equalsIgnoreCase("denied")) {
                    throw new InvalidPageParameterException("Order with status ACTIVE cannot be set to DENIED");
                }
            }
            default -> throw new InvalidStatusException("Order status cannot be changed from " +
                    "DENIED, COMPLETE, ACCEPTED_FOR_DELIVERY and DELIVERED");
        }
    }

    public static void convertOrderActionToOrderStatusForUpdate(Order orderToUpdate, String orderAction) {
        switch (orderAction.toLowerCase()) {
            case "active" -> orderToUpdate.setStatus(OrderStatus.ACTIVE);
            case "denied" -> orderToUpdate.setStatus(OrderStatus.DENIED);
            case "complete" -> orderToUpdate.setStatus(OrderStatus.COMPLETE);
            default -> throw new InvalidPageParameterException("Invalid status parameters");
        }
    }
}