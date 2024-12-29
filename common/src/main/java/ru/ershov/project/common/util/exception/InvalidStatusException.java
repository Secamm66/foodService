package ru.ershov.project.common.util.exception;

public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException(String message) {
        super (message);
    }
}
