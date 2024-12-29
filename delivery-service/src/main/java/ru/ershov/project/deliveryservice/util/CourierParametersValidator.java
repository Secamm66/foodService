package ru.ershov.project.deliveryservice.util;

import ru.ershov.project.common.models.Courier;
import ru.ershov.project.common.models.statuses.CourierStatus;
import ru.ershov.project.common.util.exception.InvalidStatusException;

public class CourierParametersValidator {

    public static void checkCourierStatusBeforeUpdate(Courier courier, String courierStatusToUpdate) {
        switch (courier.getStatus()) {
            case AVAILABLE -> {
                if (courierStatusToUpdate.equalsIgnoreCase("available")) {
                    throw new InvalidStatusException("You are already free and have started your shift");
                }
            }
            case NOT_AVAILABLE -> {
                if (courierStatusToUpdate.equalsIgnoreCase("notAvailable")) {
                    throw new InvalidStatusException("Your shift is already over");
                }
            }
            case BUSY ->
                    throw new InvalidStatusException("You cannot complete a shift or change the status until you have completed all deliveries");
            default -> throw new InvalidStatusException("Courier status is not valid");
        }
    }

    public static void convertStringToCourierStatusForUpdate(Courier courier, String courierStatusToUpdate) {
        switch (courierStatusToUpdate.toLowerCase()) {
            case "available" -> courier.setStatus(CourierStatus.AVAILABLE);
            case "notavailable" -> courier.setStatus(CourierStatus.NOT_AVAILABLE);
            default -> throw new InvalidStatusException("Courier status is not valid");
        }
    }
}