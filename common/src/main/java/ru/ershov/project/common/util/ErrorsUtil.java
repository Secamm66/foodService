package ru.ershov.project.common.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.ershov.project.common.util.exception.EntityNotCreatedException;

import java.util.List;

public class ErrorsUtil {

    public static void returnErrorsToClient(BindingResult bindingResult) {

        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errorMsg.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage() == null ? error.getCode() : error.getDefaultMessage())
                    .append(";");
        }
        throw new EntityNotCreatedException(errorMsg.toString());
    }
}