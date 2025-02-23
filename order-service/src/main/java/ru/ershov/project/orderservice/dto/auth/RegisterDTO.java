package ru.ershov.project.orderservice.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
@Schema(description = "ДТО для формы регистрации клиента")
public class RegisterDTO {

    @NotEmpty(message = "Укажите username")
    @Size(min = 3, max = 50, message = "username может содержать от 3 до 50 символов")
    @Schema(description = "Username клиента")
    private String username;

    @NotEmpty(message = "Укажите пароль")
    @Size(min = 8, max = 100, message = "Пароль может содержать от 8 до 100 символов")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$",
            message = "Пароль должен содержать минимум 8 символов, " +
                    "включая хотя бы одну цифру, одну строчную букву, " +
                    "одну заглавную букву и один специальный символ (!@#$%^&*).")
    @Schema(description = "Password клиента")
    private String password;

    @NotEmpty(message = "Укажите ваше имя")
    @Size(min = 3, max = 50, message = "имя может содержать от 3 до 50 символов")
    @Schema(description = "Имя клиента")
    private String name;

    @NotEmpty(message = "Укажите адрес")
    @Pattern(regexp = "^([\\p{L}\\s-]+),\\s([\\p{L}\\d\\s-]+),\\s(\\d+),\\s(\\d+)$",
            message = "Укажите адрес в формате 'Город, улица, дом, квартира'")
    @Schema(description = "Адрес клиента")
    private String address;

    @NotEmpty(message = "Укажите номер телефона")
    @Pattern(regexp = "^\\+7\\d{10}$",
            message = "Укажите номер в формате +7 999 999 99 99 без пробелов")
    @Schema(description = "Номер телефона клиента")
    private String phone;

    @NotEmpty(message = "Укажите e-mail")
    @Email(message = "Неверно указан e-mail")
    @Schema(description = "Почта клиента")
    private String email;

}