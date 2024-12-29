package ru.ershov.project.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.ershov.project.common.models.statuses.OrderStatus;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class OrderStatusChangedEvent {

    private Long orderId;
    private String message;
    private OrderStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}