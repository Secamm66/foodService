package ru.ershov.project.deliveryservice.dto.responseToGetDeliveries;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "ДТО, содержащее информацию о клиенте")
public class CustomerDTO {

    @Schema(description = "Адрес клиента")
    String address;

    @Schema(description = "Расстояние от текущего местоположения курьера до клиента")
    Double distance;

}