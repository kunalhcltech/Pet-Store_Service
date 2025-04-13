package com.hcltech.service;

import com.hcltech.dto.CustomerResponseDTO;
import com.hcltech.dto.OrderRequestDTO;
import com.hcltech.dto.OrderResponseDTO;
import com.hcltech.dto.PetResponseDTO;
import com.hcltech.model.Customer;
import com.hcltech.model.Order;
import com.hcltech.model.Pet;
import com.hcltech.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;
    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void testCreateOrder() {

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();



        com.hcltech.model.Order savedOrder = new Order();
        savedOrder.setPurchaseId(1L);

        when(orderRepository.save(any(com.hcltech.model.Order.class))).thenReturn(savedOrder);

        OrderResponseDTO responseDTO = orderService.createOrder(orderRequestDTO);

        assertEquals(1L, responseDTO.getOrderId());

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testGetAllOrders() {
        Order order1 = new Order();
        order1.setPurchaseId(1L);
        order1.setPurchaseDate(LocalDate.now());
        CustomerResponseDTO customer = new CustomerResponseDTO();
        customer.setCustomerId(1L);
        customer.setCustomerName("John Doe");
        Customer map = modelMapper.map(customer, Customer.class);
        order1.setCustomer(map);
        PetResponseDTO pet = new PetResponseDTO();
        pet.setPetId(101L);
        pet.setPetName("Buddy");
        Pet petEnt = modelMapper.map(pet, Pet.class);
        order1.setPet(petEnt);
        Order order2 = new Order();
        order2.setPurchaseId(2L);
        CustomerResponseDTO customer2 = new CustomerResponseDTO();
        customer2.setCustomerId(1L);
        customer2.setCustomerName("John Doe");
        Customer map2 = modelMapper.map(customer, Customer.class);
        order1.setCustomer(map2);
        PetResponseDTO pet2 = new PetResponseDTO();
        pet2.setPetId(101L);
        pet2.setPetName("Buddy");
        Pet petEnt2 = modelMapper.map(pet, Pet.class);
        order1.setPet(petEnt2);
        order1.setPurchaseDate(LocalDate.now());


        List<Order> orderList = Arrays.asList(order1, order2);

        when(orderRepository.findAll()).thenReturn(orderList);

        List<OrderResponseDTO> result = orderService.getAllOrders();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getOrderId());
        assertEquals(2L, result.get(1).getOrderId());

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrderById() {
        Long orderId = 1L;
        Order order = new Order();
        order.setPurchaseId(orderId);
        CustomerResponseDTO customer2 = new CustomerResponseDTO();
        customer2.setCustomerId(1L);
        customer2.setCustomerName("John Doe");
        Customer map2 = modelMapper.map(customer2, Customer.class);
        order.setCustomer(map2);
        PetResponseDTO pet2 = new PetResponseDTO();
        pet2.setPetId(101L);
        pet2.setPetName("Buddy");
        Pet petEnt2 = modelMapper.map(pet2, Pet.class);
        order.setPet(petEnt2);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderResponseDTO result = orderService.getOrderById(orderId);

        assertEquals(orderId, result.getOrderId());

        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testUpdateReturnedState() {
        Long orderId = 1L;
        Order order = new Order();
        order.setReturned(false);
        CustomerResponseDTO customer2 = new CustomerResponseDTO();
        customer2.setCustomerId(1L);
        customer2.setCustomerName("John Doe");
        Customer map2 = modelMapper.map(customer2, Customer.class);
        order.setCustomer(map2);
        PetResponseDTO pet2 = new PetResponseDTO();
        pet2.setPetId(101L);
        pet2.setPetName("Buddy");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        String result = orderService.updateReturnedState(orderId);

        assertEquals("Order Returned", result);

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}