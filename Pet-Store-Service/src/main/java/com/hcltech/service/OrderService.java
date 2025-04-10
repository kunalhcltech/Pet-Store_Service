package com.hcltech.service;

import com.hcltech.dto.OrderRequestDTO;
import com.hcltech.dto.OrderResponseDTO;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDto);
    List<OrderResponseDTO> getAllOrders();
    OrderResponseDTO getOrderById (Long orderID);
    OrderResponseDTO updateReturnedState (Long orderId);
}