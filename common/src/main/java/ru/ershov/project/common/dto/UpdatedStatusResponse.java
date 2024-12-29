package ru.ershov.project.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Schema(description = "ДТО, возвращающее ответ на обновление статуса заказа, курьера или ресторана")
public class UpdatedStatusResponse {

    @Schema(description = "Id заказа, курьера или ресторана")
    private Long id;

    @Schema(description = "Информационное сообщение")
    private String message;

}