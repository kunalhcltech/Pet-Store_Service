package com.hcltech.controller;


import com.hcltech.dto.OrderRequestDTO;
import com.hcltech.dto.OrderResponseDTO;
import com.hcltech.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/createOrder")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderResponseDto){
        OrderResponseDTO resultResponse = orderService.createOrder(orderResponseDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(resultResponse);


    }
    @GetMapping("/getAllOrders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders());
    }
    @GetMapping("/getOrderById/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderById(id));
    }

    @PatchMapping("returned/{orderId}")
    public ResponseEntity<OrderResponseDTO> returnOrder(@RequestParam Long id){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateReturnedState(id));
    }
}