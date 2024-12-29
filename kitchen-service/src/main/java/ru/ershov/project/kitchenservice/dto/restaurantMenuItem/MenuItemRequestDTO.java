package ru.ershov.project.kitchenservice.dto.restaurantMenuItem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;

@Data
@Accessors(chain = true)
@Schema(description = "ДТО для создания новой позиции меню ресторана")
public class MenuItemRequestDTO {

    @NotEmpty(message = "Укажите название позиции меню")
    @Size(min = 5, max = 100, message = "Название блюда может содержать от 5 до 100 символов")
    @Schema(description = "Название позиции меню")
    private String name;

    @NotNull(message = "Укажите стоимость позиции меню")
    @Min(value = 10, message = "Стоимость не может быть менее 10 рублей")
    @Schema(description = "Стоимость позиции меню")
    private Integer price;

    @Size(max = 255, message = "Длина пути файла не должна превышать 255 символов")
    @Schema(description = "Файл изображения позиции меню")
    private String imagePath;

    @Size(max = 255, message = "Описание позиции меню не может превышать 255 символов")
    @Schema(description = "Описание позиции меню")
    private String description;

}