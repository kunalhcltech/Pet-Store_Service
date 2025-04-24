package com.hcltech.service;

import com.hcltech.dto.CustomerResponseDTO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

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

        logger.info("Creating order for customerId: " + orderRequestDto.getCustomerId() + " and petId: " + orderRequestDto.getPetId());

        Customer customer = customerRepository.findById(orderRequestDto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        Pet pet = petRepository.findByPetIdAndAvailableTrue(orderRequestDto.getPetId())
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        Order order = mapper.map(orderRequestDto, Order.class);
        order.setCustomer(customer);
        order.setPet(pet);
        order.setPurchaseDate(LocalDate.now());
        List<Order> orders = customer.getOrders();
        orders.add(order);
        customer.setOrders(orders);
        if (order.getReturned())
            order.setReturned(false);
        Order savedOrder = orderRepository.save(order);
        Pet updatedPet = order.getPet();
        pet.setAvailable(false);
        petRepository.save(updatedPet);
        Customer savedCustomer = savedOrder.getCustomer();
        OrderResponseDTO orderResponseDTO = mapper.map(savedOrder, OrderResponseDTO.class);
        orderResponseDTO.setCustomerDto(mapper.map(savedCustomer, CustomerResponseDTO.class));

        logger.info("Order created successfully with orderId: " + savedOrder.getPurchaseId());

        return orderResponseDTO;
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        logger.info("Fetching all orders");

        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(order -> mapper.map(order, OrderResponseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO getOrderById(Long orderId) {
        logger.info("Fetching order by ID: " + orderId);

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        return mapper.map(order, OrderResponseDTO.class);
    }

    @Override
    public String updateReturnedState(Long orderId) {
        logger.info("Processing return for orderId: " + orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (Period.between(order.getPurchaseDate(), LocalDate.now()).getDays() < 7) {
            Pet pet = order.getPet();
            pet.setAvailable(true);
            petRepository.save(pet);
            orderRepository.delete(order);

            logger.info("Order returned and deleted successfully for orderId: " + orderId);
        } else {
            logger.warn("Order cannot be returned, more than 7 days since purchase for orderId: " + orderId);
            throw new InvalidOperationExcepetion("Order cannot be returned as it has not been 7 days since purchase");
        }

        return "Order " + orderId + " returned Successfull!!!";
    }
    @Override
    public List<OrderResponseDTO>  getAllOrdersByCustomerId(Long customerId){

        List<Order> byCustomerCustomerId = orderRepository.findByCustomer_customerId(customerId);

        List<OrderResponseDTO> collect = byCustomerCustomerId.stream().map(order -> mapper.map(order, OrderResponseDTO.class)).toList();
        if(collect.isEmpty())throw  new OrderNotFoundException("Order Not Found");

        return collect;
    }

}
