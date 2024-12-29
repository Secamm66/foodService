package ru.ershov.project.deliveryservice.dto.responseToGetDeliveries;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Schema(description = "ДТО, формирующее список доставок")
public class DeliveryListGetResponse {

    @Schema(description = "Список доставок")
    private List<DeliveryResponseDTO> delivery;

    @Schema(description = "Номер страницы")
    private int pageIndex;

    @Schema(description = "Количество заказов на странице")
    private int pageCount;

}