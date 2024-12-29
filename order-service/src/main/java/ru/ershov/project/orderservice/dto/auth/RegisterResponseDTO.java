package ru.ershov.project.orderservice.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Schema(description = "ДТО, содержащее ответ на запрос о регистрации клиента")
public class RegisterResponseDTO {

    @Schema(description = "Id клиента")
    private Long userId;

    @Schema(description = "Информационное сообщение")
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Время регистрации клиента")
    private LocalDateTime registerTime;

    @Schema(description = "JWT-токен")
    private String jwtToken;

}