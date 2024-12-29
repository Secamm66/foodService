package ru.ershov.project.common.util.exception;

public class RestaurantIsNotOpenException extends RuntimeException {
    public RestaurantIsNotOpenException(String message) {
        super(message);
    }
}
