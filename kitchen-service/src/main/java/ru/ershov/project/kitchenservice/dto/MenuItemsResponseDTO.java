package ru.ershov.project.kitchenservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Список позиций меню в заказе")
public class MenuItemsResponseDTO {

    @Schema(description = "Количество конкретной позиции")
    private int quantity;

    @Schema(description = "id позиции меню")
    private Long menuItemId;

}