package ru.ershov.project.deliveryservice.dto.responseToGetDeliveries;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "ДТО, содержащее детали доставки")
public class DeliveryResponseDTO {

    @Schema(description = "Id заказа")
    private Long orderId;

    @Schema(description = "ДТО, содержащее информацию о ресторане")
    private RestaurantDTO restaurant;

    @Schema(description = "ДТО, содержащее информацию о клиенте")
    private CustomerDTO customer;

    @Schema(description = "Сумма возаграждения за завершённую доставку")
    private Integer payment;

}