package ru.ershov.project.orderservice.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "ДТО, содержащее ответ на успешную аутентификацию клиента")
public class LoginResponseDTO {

    @Schema(description = "JWT-токен")
    private String token;

}