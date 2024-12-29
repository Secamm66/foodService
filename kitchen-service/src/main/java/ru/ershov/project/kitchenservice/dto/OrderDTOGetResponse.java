package ru.ershov.project.kitchenservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Список заказов")
public class OrderDTOGetResponse {

    @Schema(description = "Id заказа")
    private Long id;

    @Schema(description = "Список позиций меню в заказе")
    private List<MenuItemsResponseDTO> menuItems;

}