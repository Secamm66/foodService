package ru.ershov.project.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Schema(description = "ДТО, содержащее ответ на запрос о создании заказа")
public class OrderCreatePostResponse {

    @Schema(description = "Id заказа")
    private Long id;

    @Schema(description = "Ссылка на оплату заказа")
    private String secretPaymentUrl;

    @Schema(description = "Время действия ссылки")
    private String estimatedTimeOfArrival;

}