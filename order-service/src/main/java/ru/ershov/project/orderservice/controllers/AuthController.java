package ru.ershov.project.orderservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ershov.project.common.util.OrderErrorResponse;
import ru.ershov.project.orderservice.dto.auth.LoginDTO;
import ru.ershov.project.orderservice.dto.auth.LoginResponseDTO;
import ru.ershov.project.orderservice.dto.auth.RegisterDTO;
import ru.ershov.project.orderservice.dto.auth.RegisterResponseDTO;
import ru.ershov.project.orderservice.services.AuthService;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Tag(name = "Регистрация и авторизация клиента")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Запрос на регистрацию клиента",
            description = "Регистрирует нового клиента в системе и возвращает JWT-токен")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterDTO dto,
                                                        BindingResult bindingResult) {
        RegisterResponseDTO response = authService.register(dto, bindingResult);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на аутентификацию клиента",
            description = "Аутентифицирует клиента в системе по username и password. Возвращает JWT-токен")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> performLogin(@RequestBody LoginDTO dto) {
        LoginResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler
    private ResponseEntity<OrderErrorResponse> handleException(BadCredentialsException e) {
        OrderErrorResponse orderErrorResponse = new OrderErrorResponse();
        orderErrorResponse.setStatus(401).setError("Unauthorized")
                .setMessage(e.getMessage()).setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(orderErrorResponse, HttpStatus.UNAUTHORIZED);
    }
}