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
import com.hcltech.service.OrderServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequestDTO orderRequestDTO;
    private Customer customer;
    private Pet pet;
    private Order order;
    private OrderResponseDTO orderResponseDTO;

    @BeforeEach
    public void setup() {
        orderRequestDTO = new OrderRequestDTO(1L, 1L);

        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setOrders(new ArrayList<>());

        pet = new Pet();
        pet.setPetId(1L);
        pet.setAvailable(true);

        order = new Order();
        order.setPurchaseId(1L);
        order.setCustomer(customer);
        order.setPet(pet);
        order.setPurchaseDate(LocalDate.now());
        order.setReturned(false);

        orderResponseDTO = new OrderResponseDTO();
        // orderResponseDTO.setPurchaseId(1L);
        orderResponseDTO.setCustomerDto(new CustomerResponseDTO());
    }

    @Test
    public void testCreateOrderSuccess() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(petRepository.findByPetIdAndAvailableTrue(1L)).thenReturn(Optional.of(pet));
        when(mapper.map(orderRequestDTO, Order.class)).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(mapper.map(order, OrderResponseDTO.class)).thenReturn(orderResponseDTO);
        when(mapper.map(customer, CustomerResponseDTO.class)).thenReturn(new CustomerResponseDTO());

        OrderResponseDTO response = orderService.createOrder(orderRequestDTO);

        assertNotNull(response);
        verify(orderRepository).save(order);
        verify(petRepository).save(pet);
    }

    @Test
    public void testCreateOrderCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> orderService.createOrder(orderRequestDTO));
    }

    @Test
    public void testCreateOrderPetNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(petRepository.findByPetIdAndAvailableTrue(1L)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () -> orderService.createOrder(orderRequestDTO));
    }

    @Test
    public void testGetAllOrders() {
        List<Order> orderList = Collections.singletonList(order);
        when(orderRepository.findAll()).thenReturn(orderList);
        when(mapper.map(order, OrderResponseDTO.class)).thenReturn(orderResponseDTO);

        List<OrderResponseDTO> result = orderService.getAllOrders();

        assertEquals(1, result.size());
    }

    @Test
    public void testGetOrderByIdSuccess() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(mapper.map(order, OrderResponseDTO.class)).thenReturn(orderResponseDTO);

        OrderResponseDTO result = orderService.getOrderById(1L);

        assertNotNull(result);
    }

    @Test
    public void testGetOrderByIdNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    public void testUpdateReturnedStateSuccess() {
        order.setPurchaseDate(LocalDate.now().minusDays(3));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        String result = orderService.updateReturnedState(1L);

        assertEquals("Order 1 returned Successfull!!!", result);
        verify(orderRepository).delete(order);
        verify(petRepository).save(pet);
    }

    @Test
    public void testUpdateReturnedStateInvalidOperation() {
        order.setPurchaseDate(LocalDate.now().minusDays(10));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(InvalidOperationExcepetion.class, () -> orderService.updateReturnedState(1L));
    }

    @Test
    public void testUpdateReturnedStateOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.updateReturnedState(1L));
    }

    @Test
    public void testGetAllOrdersByCustomerId_OrderNotFound() {
        when(orderRepository.findByCustomer_customerId(1L)).thenReturn(List.of());

        assertThrows(OrderNotFoundException.class, () -> orderService.getAllOrdersByCustomerId(1L));
    }


}