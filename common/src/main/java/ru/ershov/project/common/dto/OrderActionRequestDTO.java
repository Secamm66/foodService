package ru.ershov.project.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "ДТО для изменения статуса заказа")
public class OrderActionRequestDTO {

    @Schema(description = "Ключ, изменяющий статус заказа. Принимаемые значения: " +
            "kitchen-service - 'denied', 'active', 'complete'; " +
            "delivery-service - 'accept', 'complete'")
    String orderAction;

}