package ru.ershov.project.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@Schema(description = "ДТО, возвращающее заказ по id")
public class OrderDTOGetResponse {

    @Schema(description = "Id заказа")
    private Long id;

    @Schema(description = "Ресторан, откуда выполняется заказ")
    private RestaurantResponseDTO restaurant;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Время создания заказа")
    private LocalDateTime orderDate;

    @Schema(description = "Список позиций неню в заказе")
    private List<ItemResponseDTO> items;

}