package ru.ershov.project.common.util.exception;

public class EntityNotCreatedException extends RuntimeException {
    public EntityNotCreatedException(String message) {
        super (message);
    }
}
