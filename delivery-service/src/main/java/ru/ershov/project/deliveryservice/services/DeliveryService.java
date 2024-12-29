package ru.ershov.project.deliveryservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ershov.project.common.dto.OrderStatusChangedEvent;
import ru.ershov.project.common.dto.UpdatedStatusResponse;
import ru.ershov.project.common.services.RabbitMQProducerServiceImpl;
import ru.ershov.project.common.util.ParametersValidator;
import ru.ershov.project.common.dto.OrderActionRequestDTO;
import ru.ershov.project.common.util.exception.InvalidStatusException;
import ru.ershov.project.deliveryservice.dto.responseToGetDeliveries.DeliveryListGetResponse;
import ru.ershov.project.deliveryservice.dto.responseToGetDeliveries.DeliveryResponseDTO;
import ru.ershov.project.deliveryservice.mappers.ToDTODeliveriesMappers.DeliveryResponseDTOMapper;
import ru.ershov.project.common.models.Courier;
import ru.ershov.project.common.models.Order;
import ru.ershov.project.common.models.statuses.CourierStatus;
import ru.ershov.project.common.models.statuses.OrderStatus;
import ru.ershov.project.common.repositories.CourierRepository;
import ru.ershov.project.common.repositories.OrderRepository;
import ru.ershov.project.common.util.exception.EntityNotFoundException;
import ru.ershov.project.deliveryservice.util.PaymentCalculator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.ershov.project.common.constants.RabbitMQConstants.ORDER_STATUS_EXCHANGE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final DeliveryResponseDTOMapper mapper;
    private final RabbitMQProducerServiceImpl<OrderStatusChangedEvent> producerService;

    private final Long courierId = 1L; // TODO: Будет браться из Authentication

    public DeliveryListGetResponse getAllDeliveriesToSend(String deliveryStatus, int pageIndex, int pageCount) {
        ParametersValidator.checkPaginationParameters(pageIndex, pageCount);
        Pageable pageable = PageRequest.of(pageIndex, pageCount);
        List<Order> orderList = getAllOrdersByStatus(deliveryStatus, pageable);
        List<DeliveryResponseDTO> deliveryList = orderList.stream()
                .map(order -> mapper.toDTO(order, getAuthCourierCoordinates(courierId)))
                .collect(Collectors.toList());
        PaymentCalculator.calculateDeliveryCost(deliveryList);
        return new DeliveryListGetResponse()
                .setDelivery(deliveryList)
                .setPageIndex(pageIndex)
                .setPageCount(pageCount);
    }

    public DeliveryResponseDTO getDeliveryOrderDetails(Long orderId) {
        Order order = getOrderById(orderId);
        DeliveryResponseDTO response = mapper.toDTO(order, getAuthCourierCoordinates(courierId));
        PaymentCalculator.calculateDeliveryCost(response);
        return response;
    }

    @Transactional
    public UpdatedStatusResponse processDelivery(Long deliveryId, OrderActionRequestDTO dto) {
        Order deliveryToUpdateStatus = getOrderById(deliveryId);
        Courier courier = getCourierById(courierId);

        return switch (deliveryToUpdateStatus.getStatus()) {
            case ACCEPTED_FOR_DELIVERY -> acceptOrderDelivery(deliveryToUpdateStatus, courier, dto.getOrderAction());
            case DELIVERED -> completeOrderDelivery(deliveryToUpdateStatus, courier, dto.getOrderAction());
            default -> throw new InvalidStatusException("Invalid order status");
        };
    }

    @Transactional
    public void acceptOrderFromKitchen(Long orderId) {
        Order order = getOrderById(orderId);
        if (order.getStatus() == OrderStatus.COMPLETE) {
            order.setStatus(OrderStatus.ACCEPTED_FOR_DELIVERY);
            orderRepository.save(order);
        } else {
            throw new InvalidStatusException("Invalid order status");
        }
    }

    private UpdatedStatusResponse acceptOrderDelivery(Order order, Courier courier, String action) {
        if (courier.getStatus() == CourierStatus.BUSY || courier.getStatus() == CourierStatus.NOT_AVAILABLE) {
            throw new InvalidStatusException("Your current status does not allow you to take this order: BUSY or NOT_AVAILABLE");
        }
        if (action.equalsIgnoreCase("accept")) {
            courier.setStatus(CourierStatus.BUSY);
            order.setStatus(OrderStatus.DELIVERED).setCourier(courier);
            orderRepository.save(order);
            orderRepository.flush();
            notifyOrderStatusChanged(order.getId(), OrderStatus.DELIVERED);
            return new UpdatedStatusResponse()
                    .setId(order.getId())
                    .setMessage("You have accepted this order for delivery!");
        } else {
            throw new InvalidStatusException("Invalid order action for ACCEPTED_FOR_DELIVERY status");
        }
    }

    private UpdatedStatusResponse completeOrderDelivery(Order order, Courier courier, String action) {
        if (action.equalsIgnoreCase("complete")) {
            order.setStatus(OrderStatus.RECEIVED);
            courier.setStatus(CourierStatus.AVAILABLE);
            orderRepository.save(order);
            orderRepository.flush();
            notifyOrderStatusChanged(order.getId(), OrderStatus.RECEIVED);
            return new UpdatedStatusResponse()
                    .setId(order.getId())
                    .setMessage("You delivered this order!");
        } else {
            throw new InvalidStatusException("Invalid order action for DELIVERED status");
        }
    }

    private List<Order> getAllOrdersByStatus(String deliveryStatus, Pageable pageable) {
        return switch (deliveryStatus.toLowerCase()) {
            case "active" -> orderRepository.findAllByStatus(OrderStatus.ACCEPTED_FOR_DELIVERY, pageable);
            case "complete" -> orderRepository.findAllByStatusAndCourierId(OrderStatus.RECEIVED, courierId, pageable);
            default -> throw new InvalidStatusException("Invalid status parameters");
        };
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with id=" + orderId + " not found"));
    }

    private Courier getCourierById(Long courierId) {
        return courierRepository.findById(courierId)
                .orElseThrow(() -> new EntityNotFoundException("Courier with id=" + courierId + " not found"));
    }

    private String getAuthCourierCoordinates(Long courierId) {
        return courierRepository.findCourierCoordinatesById(courierId)
                .orElseThrow(() -> new EntityNotFoundException("Courier with id=" + courierId + " not found"));
    }

    private void notifyOrderStatusChanged(Long orderId, OrderStatus status) {
        OrderStatusChangedEvent event = new OrderStatusChangedEvent();
        event.setOrderId(orderId)
                .setMessage("some message")
                .setStatus(status)
                .setUpdatedAt(LocalDateTime.now());
        producerService.sendMessage(ORDER_STATUS_EXCHANGE, event, status.getRoutingKey());
    }
}