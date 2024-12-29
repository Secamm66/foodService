package ru.ershov.project.orderservice.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "ДТО для формы аутентификации клиента")
public class LoginDTO {

    @Schema(description = "Username клиента")
    private String username;

    @Schema(description = "Password клиента")
    private String password;

}