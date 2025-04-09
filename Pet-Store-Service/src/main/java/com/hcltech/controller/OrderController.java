package com.hcltech.controller;

import com.hcltech.dto.OrderRequestDTO;
import com.hcltech.dto.OrderResponseDTO;
import com.hcltech.model.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop/order")
public class OrderController {

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder (@RequestBody OrderRequestDTO orderRequestDTO)
    {
        return null;
    }
    @PutMapping("/return/{orderId}")
    public ResponseEntity<String> returnOrder (@PathVariable("orderId") Integer orderId)
    {
        return null;
    }

    @GetMapping("/get")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders()
    {
        return null;
    }

    @GetMapping("/get/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable("orderId") Integer orderid)
    {
        return null;
    }
}
