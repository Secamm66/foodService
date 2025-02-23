package ru.ershov.project.orderservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.ershov.project.common.models.Order;
import ru.ershov.project.common.repositories.OrderRepository;
import ru.ershov.project.orderservice.dto.OrderDTOGetResponse;
import ru.ershov.project.orderservice.dto.OrderListGetResponse;
import ru.ershov.project.orderservice.mapper.OrderResponseDTOMapper;
import ru.ershov.project.orderservice.services.OrderService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderResponseDTOMapper orderResponseDTOMapper;

    @Test
    void getAllOrdersToSend_shouldReturnOrders_whenValidParameters() {
        // Arrange
        int pageIndex = 0, pageCount = 10;
        List<Order> orders = List.of(new Order(), new Order());
        List<OrderDTOGetResponse> orderDTOs = List.of(new OrderDTOGetResponse(), new OrderDTOGetResponse());

        when(orderRepository.findAllByCustomerId(anyLong(), any(Pageable.class))).thenReturn(orders);
        when(orderResponseDTOMapper.toDTO(any(Order.class)))
                .thenReturn(new OrderDTOGetResponse()); // Мок маппера

        // Act
        OrderListGetResponse response = orderService.getAllOrdersToSend(pageIndex, pageCount);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getOrders().size());
        assertEquals(pageIndex, response.getPageIndex());
        assertEquals(pageCount, response.getPageCount());

        verify(orderRepository).findAllByCustomerId(anyLong(), any(Pageable.class));
        verify(orderResponseDTOMapper, times(2)).toDTO(any(Order.class));
    }
}