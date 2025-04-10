package com.hcltech.controller;


import com.hcltech.dto.OrderRequestDTO;
import com.hcltech.dto.OrderResponseDTO;
import com.hcltech.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/create")
    @Operation(summary ="Create Order",description = "This method creates Order")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderResponseDto){
        OrderResponseDTO resultResponse = orderService.createOrder(orderResponseDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(resultResponse);


    }
    @GetMapping("/get")
    @Operation(summary ="Get Orders",description = "This method retrieves all the orders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders());
    }
    @GetMapping("/getOrderById/{id}")
    @Operation(summary ="Get Order by ID",description = "This method retrieves the order with reference of ID")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderById(id));
    }

    @PatchMapping("return/{orderId}")
    @Operation(summary ="Return Order",description = "This method returns the order with reference of ID")
    public ResponseEntity<OrderResponseDTO> returnOrder(@PathVariable("orderId") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateReturnedState(id));
    }
}