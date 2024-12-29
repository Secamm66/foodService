package ru.ershov.project.orderservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.ershov.project.common.dto.OrderStatusChangedEvent;
import ru.ershov.project.common.dto.UpdatedStatusResponse;
import ru.ershov.project.common.models.*;
import ru.ershov.project.common.models.statuses.OrderStatus;
import ru.ershov.project.common.models.statuses.RestaurantStatus;
import ru.ershov.project.common.repositories.OrderRepository;
import ru.ershov.project.common.repositories.RestaurantMenuItemRepository;
import ru.ershov.project.common.repositories.RestaurantRepository;
import ru.ershov.project.common.services.RabbitMQProducerServiceImpl;
import ru.ershov.project.common.util.ErrorsUtil;
import ru.ershov.project.common.util.ParametersValidator;
import ru.ershov.project.common.util.exception.EntityNotCreatedException;
import ru.ershov.project.common.util.exception.EntityNotFoundException;
import ru.ershov.project.common.util.exception.RestaurantIsNotOpenException;
import ru.ershov.project.orderservice.dto.*;
import ru.ershov.project.orderservice.mapper.OrderRequestDTOMapper;
import ru.ershov.project.orderservice.mapper.OrderResponseDTOMapper;
import ru.ershov.project.orderservice.security.UsersDetails;
import ru.ershov.project.orderservice.util.OrderParametersValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.ershov.project.common.constants.RabbitMQConstants.ORDER_STATUS_EXCHANGE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantMenuItemRepository restaurantMenuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderResponseDTOMapper orderResponseDTOMapper;
    private final OrderRequestDTOMapper orderRequestDTOMapper;
    private final RabbitMQProducerServiceImpl<OrderStatusChangedEvent> producerService;

    public OrderListGetResponse getAllOrdersToSend(int pageIndex, int pageCount) {
        ParametersValidator.checkPaginationParameters(pageIndex, pageCount);
        Pageable pageable = PageRequest.of(pageIndex, pageCount);
        List<Order> orderList = orderRepository.findAllByCustomerId(getCustomerId(), pageable);
        List<OrderDTOGetResponse> orderDTOGetResponseList = orderList.stream()
                .map(orderResponseDTOMapper::toDTO)
                .collect(Collectors.toList());
        return new OrderListGetResponse()
                .setOrders(orderDTOGetResponseList)
                .setPageIndex(pageIndex)
                .setPageCount(pageCount);
    }

    public OrderDTOGetResponse getOrderByIdToSend(Long orderId) {
        Order order = getOrderById(orderId);
        return orderResponseDTOMapper.toDTO(order);
    }

    @Transactional
    public OrderCreatePostResponse createOrder(OrderRequestDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorsUtil.returnErrorsToClient(bindingResult);
        }
        Restaurant restaurant = getRestaurantAndCheckMenuItemsById(dto);
        Order order = orderRequestDTOMapper.toEntity(dto);
        enrichOrder(order, restaurant);
        orderRepository.save(order);
        notifyOrderStatusChanged(order.getId(), order.getStatus());
        return new OrderCreatePostResponse()
                .setId(order.getId())
                .setSecretPaymentUrl("some payment")
                .setEstimatedTimeOfArrival("some time");
    }

    @Transactional
    public UpdatedStatusResponse updateOrderStatusToPaid(Long orderId) {
        Order orderToUpdate = getOrderById(orderId);
        OrderParametersValidator.checkOrderStatusBeforeUpdate(orderToUpdate);
        orderToUpdate.setStatus(OrderStatus.PAID);
        orderRepository.save(orderToUpdate);
        orderRepository.flush();
        notifyOrderStatusChanged(orderId, OrderStatus.PAID);
        return new UpdatedStatusResponse()
                .setId(orderId)
                .setMessage("Order status from id=" + orderId + " changed to PAID"); //проверить
    }

    private Restaurant getRestaurantAndCheckMenuItemsById(OrderRequestDTO dto) {
        Restaurant restaurant = getRestaurantById(dto.getRestaurantId());

        if (restaurant.getStatus() == RestaurantStatus.CLOSED) {
            throw new RestaurantIsNotOpenException("Restaurant " + restaurant.getName() + " is closed now");
        }
        for (MenuItemsRequestDTO menuItem : dto.getMenuItems()) {
            RestaurantMenuItem restaurantMenuItem = getRestaurantMenuItemById(menuItem.getMenuItemId());

            if (!restaurantMenuItem.getRestaurant().getId().equals(restaurant.getId())) {
                throw new EntityNotCreatedException("Menu item does not belong to the selected restaurant");
            }
        }
        return restaurant;
    }

    private void enrichOrder(Order order, Restaurant restaurant) {
        Customer customer = new Customer();
        customer.setId(getCustomerId());
        order.setCustomer(customer)
                .setStatus(OrderStatus.ACCEPTED)
                .setOrderDate(LocalDateTime.now());

        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setOrder(order);
            restaurant.getRestaurantMenuItems().stream()
                    .filter(menuItem -> menuItem.getId().equals(orderItem.getRestaurantMenuItem().getId()))
                    .findFirst()
                    .ifPresent(menuItem -> orderItem.setPrice(menuItem.getPrice() * orderItem.getQuantity()));
        }
    }

    private Restaurant getRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + restaurantId + " not found"));
    }

    private RestaurantMenuItem getRestaurantMenuItemById(Long menuItemId) {
        return restaurantMenuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem with id=" + menuItemId + " not found"));
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findByIdAndCustomerId(orderId, getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Order with id=" + orderId + " not found"));
    }

    private void notifyOrderStatusChanged(Long orderId, OrderStatus status) {
        OrderStatusChangedEvent event = new OrderStatusChangedEvent();
        event.setOrderId(orderId)
                .setMessage("some message")
                .setStatus(status)
                .setUpdatedAt(LocalDateTime.now());
        producerService.sendMessage(ORDER_STATUS_EXCHANGE, event, status.getRoutingKey());
    }

    private Long getCustomerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersDetails usersDetails = (UsersDetails) authentication.getPrincipal();
        return usersDetails.getUser().getCustomer().getId();
    }
}