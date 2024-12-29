package ru.ershov.project.orderservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import ru.ershov.project.common.util.exception.EntityNotFoundException;
import ru.ershov.project.orderservice.dto.*;
import ru.ershov.project.orderservice.services.OrderService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllOrders_shouldReturnAllOrders() throws Exception {
        List<OrderDTOGetResponse> orders = List.of(
                new OrderDTOGetResponse().setId(1L),
                new OrderDTOGetResponse().setId(2L)
        );
        OrderListGetResponse responseList = new OrderListGetResponse();
        responseList.setOrders(orders)
                .setPageIndex(0)
                .setPageCount(10);

        when(orderService.getAllOrdersToSend(0, 10)).thenReturn(responseList);

        mockMvc.perform(get("/api/v1/orders")
                        .param("page_index", "0")
                        .param("page_count", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders", hasSize(2)))
                .andExpect(jsonPath("$.orders[0].id", is(1)))
                .andExpect(jsonPath("$.orders[1].id", is(2)))
                .andExpect(jsonPath("$.pageIndex", is(0)))
                .andExpect(jsonPath("$.pageCount", is(10)));

        verify(orderService, times(1)).getAllOrdersToSend(0, 10);
    }

    @Test
    void getOrder_shouldReturnOrder_whenOrderExists() throws Exception {

        OrderDTOGetResponse order = new OrderDTOGetResponse();
        order.setId(1L);
        order.setOrderDate(LocalDateTime.of(2023, 10, 1, 12, 0, 0));
        order.setRestaurant(new RestaurantResponseDTO());
        order.setItems(List.of(new ItemResponseDTO()));

        when(orderService.getOrderByIdToSend(1L)).thenReturn(order);

        mockMvc.perform(get("/api/v1/orders/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.restaurant", notNullValue()))
                .andExpect(jsonPath("$.orderDate", is("2023-10-01T12:00:00")))
                .andExpect(jsonPath("$.items", hasSize(1)));

        verify(orderService, times(1)).getOrderByIdToSend(1L);
    }

    @Test
    void getOrder_shouldReturnNotFound_whenOrderDoesNotExist() throws Exception {

        when(orderService.getOrderByIdToSend(1L))
                .thenThrow(new EntityNotFoundException("Restaurant with id= " + 1 + " not found"));

        mockMvc.perform(get("/api/v1/orders/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Restaurant with id= " + 1 + " not found")))
                .andExpect(jsonPath("$.error", is("Not Found")));

        verify(orderService, times(1)).getOrderByIdToSend(1L);
    }

    @Test
    void createOrder_shouldReturnNewOrder() throws Exception {
        List<MenuItemsRequestDTO> menuItems = new ArrayList<>();
        menuItems.add(new MenuItemsRequestDTO().setMenuItemId(7L).setQuantity(2));
        menuItems.add(new MenuItemsRequestDTO().setMenuItemId(8L).setQuantity(1));
        OrderRequestDTO newOrder = new OrderRequestDTO();
        newOrder.setMenuItems(menuItems);
        newOrder.setRestaurantId(1L);
        BindingResult bindingResult = Mockito.mock(BindingResult.class);

        OrderCreatePostResponse response = new OrderCreatePostResponse();
        response.setId(1L).setEstimatedTimeOfArrival("some time").setSecretPaymentUrl("some url");

        when(orderService.createOrder(newOrder, bindingResult)).thenReturn(response);

        String workerJson = objectMapper.writeValueAsString(newOrder);
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(workerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.estimatedTimeOfArrival", is("some time")))
                .andExpect(jsonPath("$.secretPaymentUrl", is("some url")));

        verify(orderService, times(1)).createOrder(newOrder, bindingResult);
    }
}