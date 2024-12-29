package ru.ershov.project.common.util;

import ru.ershov.project.common.util.exception.InvalidPageParameterException;

public class ParametersValidator {

    public static void checkPaginationParameters(int pageIndex, int pageCount) {
        if (pageIndex < 0 || pageCount < 1) {
            throw new InvalidPageParameterException("Invalid pagination parameters");
        }
    }
}