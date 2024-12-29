package ru.ershov.project.kitchenservice.dto.restaurantMenuItem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Schema(description = "ДТО, формирующее список позиций меню ресторана")
public class MenuItemListGetResponse {

    @Schema(description = "Список позиций меню ресторана")
    private List<MenuItemDTO> menu;

    @Schema(description = "Номер страницы")
    private int pageIndex;

    @Schema(description = "Количество заказов на странице")
    private int pageCount;

}