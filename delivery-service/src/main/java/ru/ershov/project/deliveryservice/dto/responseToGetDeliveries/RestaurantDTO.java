package ru.ershov.project.deliveryservice.dto.responseToGetDeliveries;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "ДТО, содержащее информацию о ресторане")
public class RestaurantDTO {

    @Schema(description = "Название ресторана")
    String name;

    @Schema(description = "Адрес ресторана")
    String address;

    @Schema(description = "Расстояние от текущего местоположения курьера до ресторана")
    Double distance;

}