package ru.ershov.project.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
@Schema(description = "ДТО для создания нового заказа")
public class OrderRequestDTO {

    @Schema(description = "Название ресторана")
    private Long restaurantId;

    @Valid
    @Schema(description = "Список позиций меню")
    private List<MenuItemsRequestDTO> menuItems;

}