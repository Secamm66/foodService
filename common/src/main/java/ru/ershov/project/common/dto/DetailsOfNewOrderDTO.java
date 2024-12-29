package ru.ershov.project.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "ДТО, содержащее детали нового заказа")
public class DetailsOfNewOrderDTO {

    @Schema(description = "Название позиции меню в заказе")
    private String menuItemName;

    @Schema(description = "Количество одной позиции")
    private int quantity;

}