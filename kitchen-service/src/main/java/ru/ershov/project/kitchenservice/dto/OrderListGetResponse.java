package ru.ershov.project.kitchenservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@Accessors(chain = true)
@Schema(description = "ДТО, формирующее список заказов")
public class OrderListGetResponse {

    @Schema(description = "Список заказов")
    private List<OrderDTOGetResponse> orders;

    @Schema(description = "Номер страницы")
    private int pageIndex;

    @Schema(description = "Количество заказов на странице")
    private int pageCount;

}