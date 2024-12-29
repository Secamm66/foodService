package ru.ershov.project.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Список позиций меню в заказе")
public class ItemResponseDTO {

    @Schema(description = "Название позиции меню")
    private String name;

    @Schema(description = "Стоимость позиции меню (цена*количество)")
    private Integer cost;

    @Schema(description = "Количество одной позиции меню")
    private int quantity;

    @Schema(description = "Описание позиции меню")
    private String description;

    @Schema(description = "URL изображения позиции меню")
    private String imagePath;

}