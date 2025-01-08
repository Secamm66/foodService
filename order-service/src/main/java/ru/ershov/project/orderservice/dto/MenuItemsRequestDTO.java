package ru.ershov.project.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;

@Data
@Accessors(chain = true)
@Schema(description = "ДТО, содержащее список позиций меню в заказе и их количество")
public class MenuItemsRequestDTO {

    @Min(value = 1, message = "Значение должно быть больше 0")
    @Schema(description = "Количество одной позииции меню в заказе")
    private int quantity;

    @Schema(description = "id позиции меню ресторана в заказе")
    private Long menuItemId;

}