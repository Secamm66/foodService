package ru.ershov.project.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "ДТО, содержащее названиие ресторана, откуда заказывается еда")
public class RestaurantResponseDTO {

    @Schema(description = "Названиие ресторана")
    private String name;

}