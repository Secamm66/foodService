package ru.ershov.project.kitchenservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ershov.project.common.dto.DetailsOfNewOrderDTO;
import ru.ershov.project.common.dto.OrderActionRequestDTO;
import ru.ershov.project.common.dto.OrderStatusChangedEvent;
import ru.ershov.project.common.dto.UpdatedStatusResponse;
import ru.ershov.project.common.models.Order;
import ru.ershov.project.common.models.statuses.OrderStatus;
import ru.ershov.project.common.repositories.OrderRepository;
import ru.ershov.project.common.services.RabbitMQProducerServiceImpl;
import ru.ershov.project.common.util.ParametersValidator;
import ru.ershov.project.common.util.exception.EntityNotFoundException;
import ru.ershov.project.kitchenservice.dto.OrderDTOGetResponse;
import ru.ershov.project.kitchenservice.dto.OrderListGetResponse;
import ru.ershov.project.kitchenservice.mappers.OrderDTOGetResponseMapper;
import ru.ershov.project.kitchenservice.util.OrderParametersValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.ershov.project.common.constants.RabbitMQConstants.ORDER_STATUS_EXCHANGE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDTOGetResponseMapper orderDTOGetResponseMapper;
    private final RabbitMQProducerServiceImpl<OrderStatusChangedEvent> producerService;
    private final Long restaurantId = 1L; // TODO: Будет браться из Authentication

    public OrderListGetResponse getAllOrdersToSend(List<String> orderStatuses, int pageIndex, int pageCount) {
        ParametersValidator.checkPaginationParameters(pageIndex, pageCount);
        List<OrderStatus> statusList = OrderParametersValidator.checkAndConvertOrderStatusParametersForFiltration(orderStatuses);
        Pageable pageable = PageRequest.of(pageIndex, pageCount);
        List<Order> orders = getOrdersByRestaurantId(restaurantId, statusList, pageable);
        List<OrderDTOGetResponse> orderDTOs = orders.stream()
                .map(orderDTOGetResponseMapper::toDTO)
                .collect(Collectors.toList());
        return new OrderListGetResponse()
                .setOrders(orderDTOs)
                .setPageIndex(pageIndex)
                .setPageCount(pageCount);
    }

    public DetailsOfNewOrderDTO getDetailsOfNewOrderToSend(Long orderId) {
        return orderRepository.findOrderDetailOfNewOrderById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with id=" + orderId + " not found"));
    }

    @Transactional
    public UpdatedStatusResponse processOrderAction(Long orderId, OrderActionRequestDTO dto) {
        Order orderToUpdate = getOrderById(orderId);
        String newOrderStatus = dto.getOrderAction();
        OrderParametersValidator.checkOrderActionBeforeUpdate(orderToUpdate, newOrderStatus);
        OrderParametersValidator.convertOrderActionToOrderStatusForUpdate(orderToUpdate, newOrderStatus);
        Order updatedOrder = orderRepository.save(orderToUpdate);
        orderRepository.flush();
        notifyOrderStatusChanged(orderId, updatedOrder.getStatus());
        return new UpdatedStatusResponse()
                .setId(orderId)
                .setMessage("Order status from id=" + orderId + " changed to " + newOrderStatus);
    }

    private List<Order> getOrdersByRestaurantId(Long restaurantId, List<OrderStatus> orderStatuses, Pageable pageable) {
        return orderRepository.findAllByRestaurantIdAndStatusIn(restaurantId, orderStatuses, pageable);
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with id=" + orderId + " not found"));
    }

    private void notifyOrderStatusChanged(Long orderId, OrderStatus status) {
        OrderStatusChangedEvent event = new OrderStatusChangedEvent();
        event.setOrderId(orderId)
                .setMessage("With love!")
                .setStatus(status)
                .setUpdatedAt(LocalDateTime.now());
        producerService.sendMessage(ORDER_STATUS_EXCHANGE, event, status.getRoutingKey());
    }
}