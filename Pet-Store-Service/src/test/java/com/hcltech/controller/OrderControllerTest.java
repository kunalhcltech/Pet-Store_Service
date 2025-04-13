package com.hcltech.controller;


import com.hcltech.controller.OrderController;
import com.hcltech.dto.OrderRequestDTO;
import com.hcltech.dto.OrderResponseDTO;
import com.hcltech.exceptions.OrderNotFoundException;
import com.hcltech.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //  createOrder() - success
    @Test
    void testCreateOrder() {
        OrderRequestDTO request = new OrderRequestDTO();
        OrderResponseDTO response = new OrderResponseDTO();

        when(orderService.createOrder(request)).thenReturn(response);

        ResponseEntity<OrderResponseDTO> result = orderController.createOrder(request);

        assertThat(result.getStatusCodeValue()).isEqualTo(201);
        assertThat(result.getBody()).isEqualTo(response);
        verify(orderService, times(1)).createOrder(request);
    }

    //  getAllOrders() - success
    @Test
    void testGetAllOrders() {
        List<OrderResponseDTO> orders = List.of(new OrderResponseDTO(), new OrderResponseDTO());

        when(orderService.getAllOrders()).thenReturn(orders);

        ResponseEntity<List<OrderResponseDTO>> result = orderController.getAllOrders();

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).hasSize(2);
        verify(orderService, times(1)).getAllOrders();
    }

    //  getOrderById() - success
    @Test
    void testGetOrderById() {
        Long id = 1L;
        OrderResponseDTO response = new OrderResponseDTO();

        when(orderService.getOrderById(id)).thenReturn(response);

        ResponseEntity<OrderResponseDTO> result = orderController.getOrderById(id);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(response);
        verify(orderService, times(1)).getOrderById(id);
    }

    //  returnOrder() - success
    @Test
    void testReturnOrder() {
        Long orderId = 1L;
        String successMessage = "Order returned successfully";

        when(orderService.updateReturnedState(orderId)).thenReturn(successMessage);

        ResponseEntity<String> result = orderController.returnOrder(orderId);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(successMessage);
        verify(orderService, times(1)).updateReturnedState(orderId);
    }

    //  createOrder throws RuntimeException
    @Test
    void testCreateOrderThrowsException() {
        OrderRequestDTO request = new OrderRequestDTO();

        when(orderService.createOrder(request)).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            orderController.createOrder(request);
        });

        assertThat(ex.getMessage()).isEqualTo("DB error");
        verify(orderService, times(1)).createOrder(request);
    }

    //  getOrderById throws OrderNotFoundException
    @Test
    void testGetOrderByIdThrowsException() {
        Long id = 999L;

        when(orderService.getOrderById(id)).thenThrow(new OrderNotFoundException("Order not found"));

        OrderNotFoundException ex = assertThrows(OrderNotFoundException.class, () -> {
            orderController.getOrderById(id);
        });

        assertThat(ex.getMessage()).isEqualTo("Order not found");
        verify(orderService, times(1)).getOrderById(id);
    }

    //  returnOrder throws RuntimeException
    @Test
    void testReturnOrderThrowsException() {
        Long id = 123L;

        when(orderService.updateReturnedState(id)).thenThrow(new RuntimeException("Update failed"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            orderController.returnOrder(id);
        });

        assertThat(ex.getMessage()).isEqualTo("Update failed");
        verify(orderService, times(1)).updateReturnedState(id);
    }
}
