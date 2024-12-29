package ru.ershov.project.kitchenservice.dto.restaurantMenuItem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "ДТО, содержащее описание позиции меню ресторана")
public class MenuItemDTO {

    @Schema(description = "Id позиции меню ресторана")
    private Long id;

    @Schema(description = "Название позиции меню ресторана")
    private String name;

    @Schema(description = "Стоимость позиции меню ресторана")
    private Integer price;

    @Schema(description = "Изображение позиции меню ресторана")
    private String imagePath;

    @Schema(description = "Описание позиции меню ресторана")
    private String description;

}