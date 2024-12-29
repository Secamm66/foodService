package ru.ershov.project.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "ДТО для изменения статуса ресторана или курьера")
public class StatusRequestDTO {

    @Schema(description = "Ключ, изменяющий статус ресторана или курьера. " +
            "Принимаемые значения для ресторана: 'open', 'closed'. " +
            "Принмаемые значения для курьера: 'available', 'notavailable'")
    String status;

}