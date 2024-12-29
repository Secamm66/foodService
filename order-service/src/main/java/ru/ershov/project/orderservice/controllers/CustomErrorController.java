package ru.ershov.project.orderservice.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ershov.project.common.util.OrderErrorResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<OrderErrorResponse> handleError(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus httpStatus = (status != null)
                ? HttpStatus.valueOf(Integer.parseInt(status.toString()))
                : HttpStatus.INTERNAL_SERVER_ERROR;

        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        String message = (errorMessage != null)
                ? errorMessage.toString()
                : getDefaultMessageForStatus(httpStatus);

        OrderErrorResponse errorResponse = new OrderErrorResponse();
        errorResponse.setStatus(httpStatus.value())
                .setError(httpStatus.getReasonPhrase())
                .setMessage(message)
                .setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    private String getDefaultMessageForStatus(HttpStatus status) {
        return switch (status) {
            case BAD_REQUEST -> "Bad Request";
            case UNAUTHORIZED -> "Unauthorized";
            case FORBIDDEN -> "Forbidden";
            case NOT_FOUND -> "Not Found";
            case INTERNAL_SERVER_ERROR -> "Internal Server Error";
            default -> "Unexpected Error";
        };
    }
}