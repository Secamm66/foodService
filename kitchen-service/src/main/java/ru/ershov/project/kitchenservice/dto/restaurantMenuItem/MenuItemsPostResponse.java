package ru.ershov.project.kitchenservice.dto.restaurantMenuItem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "ДТО, содержащее ответ на запрос добавления новой позиции меню ресторана")
public class MenuItemsPostResponse {

    @Schema(description = "Информационное сообщение")
    String message;

}