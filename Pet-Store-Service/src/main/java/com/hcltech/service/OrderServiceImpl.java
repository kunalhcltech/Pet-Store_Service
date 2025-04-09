package com.hcltech.service;

import com.hcltech.dto.OrderRequestDTO;
import com.hcltech.dto.OrderResponseDTO;
import com.hcltech.exceptions.CustomerNotFoundException;
import com.hcltech.exceptions.InvalidOperationExcepetion;
import com.hcltech.exceptions.OrderNotFoundException;
import com.hcltech.exceptions.PetNotFoundException;
import com.hcltech.model.Customer;
import com.hcltech.model.Order;
import com.hcltech.model.Pet;
import com.hcltech.repository.CustomerRepository;
import com.hcltech.repository.OrderRepository;
import com.hcltech.repository.PetRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDto) {


        Customer customer = customerRepository.findById(orderRequestDto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        Pet pet = petRepository.findById(orderRequestDto.getPetId())
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        Order order = mapper.map(orderRequestDto, Order.class);
        order.setCustomer(customer);
        order.setPet(pet);
        order.setPurchaseDate(LocalDate.now());
        if(order.getReturned())order.setReturned(false);
        Order savedOrder = orderRepository.save(order);

        return mapper.map(savedOrder,OrderResponseDTO.class);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order->mapper.map(order,OrderResponseDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));


        return mapper.map(order,OrderResponseDTO.class);
    }


    @Override
    public OrderResponseDTO updateReturnedState(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (Period.between(order.getPurchaseDate(), LocalDate.now()).getDays()<7) {
            order.setReturned(true);
        } else {
            throw new InvalidOperationExcepetion("Order cannot be returned as it has not been 7 days since purchase");
        }

        Order updatedOrder = orderRepository.save(order);
        return mapper.map(updatedOrder,OrderResponseDTO.class);
    }

}