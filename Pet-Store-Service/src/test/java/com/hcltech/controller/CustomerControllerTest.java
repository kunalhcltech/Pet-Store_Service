package com.hcltech.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Arrays;

import org.apache.catalina.User;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import com.hcltech.controller.CustomerController;
import com.hcltech.controller.OrderController;
import com.hcltech.dto.CustomerRequestDTO;
import com.hcltech.dto.CustomerResponseDTO;
import com.hcltech.dto.JWTAuthRequest;
import com.hcltech.dto.JWTAuthResponse;
import com.hcltech.dto.OrderResponseDTO;
import com.hcltech.exceptions.CustomerNotFoundException;
import com.hcltech.exceptions.GlobalExceptionHandler;
import com.hcltech.security.CustomerDetailsService;
import com.hcltech.security.JWTHelper;
import com.hcltech.service.CustomerService;
import com.hcltech.service.OrderService;


import io.jsonwebtoken.lang.Collections;

public class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @Mock
    private OrderService orderService;

    @Mock
    private JWTHelper helper;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private CustomerDetailsService customerDetailsService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //login test

//    @Test
//    void testLoginSuccess() {
//        JWTAuthRequest request = new JWTAuthRequest("test@example.com", "password");
//
//        Authentication auth = mock(Authentication.class);
//        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
//
//        UserDetails userDetails = User.("test@example.com").password("pass").authorities("USER").build();
//        when(customerDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
//        when(helper.generateToken(userDetails)).thenReturn("dummy-token");
//
//        ResponseEntity<JWTAuthResponse> response = customerController.login(request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("dummy-token", response.getBody().getToken());
//    }


    //successful customer get by id
    @Test
    void testGetCustomerByIdSuccess() {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        when(customerService.getCustomerById(1L)).thenReturn(dto);

        ResponseEntity<CustomerResponseDTO> response = customerController.getCustomerById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    //Customer not found by id

    @Test
    void testGetCustomerByIdNotFound() {
        when(customerService.getCustomerById(100L)).thenThrow(new NoSuchElementException("Customer not found"));

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> {
            customerController.getCustomerById(100);
        });

        assertEquals("Customer not found", ex.getMessage());
    }

    //delete customer successful
    @Test
    void testDeleteCustomer() {
        Long customerId = 1L;
        when(customerService.deleteCustomer(customerId)).thenReturn("Customer deleted");

        ResponseEntity<String> response = customerController.deleteCustomer(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer deleted", response.getBody());
    }


    // delete customer unsuccessful
    @Test
    void testDeleteCustomerNotFound() {
        when(customerService.deleteCustomer(99L)).thenThrow(new CustomerNotFoundException("Customer not found"));

        CustomerNotFoundException ex = assertThrows(CustomerNotFoundException.class, () -> {
            customerController.deleteCustomer(99L);
        });

        assertEquals("Customer not found", ex.getMessage());
    }


    //create customer successfully
    @Test
    void testCreateCustomer() {
        CustomerRequestDTO requestDTO = new CustomerRequestDTO();
        CustomerResponseDTO responseDTO = new CustomerResponseDTO();

        when(customerService.createCustomer(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<CustomerResponseDTO> response = customerController.createCustomer(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    //create customer unsuccessfully

    @Test
    void testCreateCustomerFailure() {
        CustomerRequestDTO req = new CustomerRequestDTO();
        when(customerService.createCustomer(req)).thenThrow(new RuntimeException("DB Error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            customerController.createCustomer(req);
        });

        assertEquals("DB Error", ex.getMessage());
    }


    //get all customer successfully
    @Test
    void testGetAllCustomers() {
        CustomerResponseDTO customer1 = new CustomerResponseDTO();
        CustomerResponseDTO customer2 = new CustomerResponseDTO();

        List<CustomerResponseDTO> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);

        when(customerService.getAllCutomers()).thenReturn(customers);

        ResponseEntity<List<CustomerResponseDTO>> response = customerController.getAllCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(customer1, response.getBody().get(0));
    }


    //test for login successfully
    @Test
    void testLoginSuccess() {
        // Arrange
        JWTAuthRequest request = new JWTAuthRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "test@example.com",
                "password",
                new ArrayList<>()
        );

        org.springframework.security.core.Authentication authentication = Mockito.mock(org.springframework.security.core.Authentication.class);

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(customerDetailsService.loadUserByUsername("test@example.com"))
                .thenReturn(userDetails);

        when(helper.generateToken(userDetails))
                .thenReturn("mocked-jwt-token");

        // Act
        ResponseEntity<JWTAuthResponse> response = customerController.login(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mocked-jwt-token", response.getBody().getToken());
        assertEquals("test@example.com", response.getBody().getUsername());
    }

    // Login Unsuccessful
    @Test
    void testLoginInvalidCredentials() {
        JWTAuthRequest request = new JWTAuthRequest("invalid@example.com", "wrongpass");

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> customerController.login(request));
    }

    //getting all order of customer successfully
    @Test
    void testGetAllOrdersByCustomerId_whenOrdersExist() {
        // Arrange
        Long customerId = 1L;
        OrderResponseDTO order1 = new OrderResponseDTO();
        order1.setOrderId(100L);

        OrderResponseDTO order2 = new OrderResponseDTO();
        order2.setOrderId(101L);

        List<OrderResponseDTO> orderList = Arrays.asList(order1, order2);

        when(orderService.getAllOrdersByCustomerId(customerId)).thenReturn(orderList);

        // Act
        ResponseEntity<List<OrderResponseDTO>> response = customerController.getAllOrdersByCustomerId(customerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(orderService, times(1)).getAllOrdersByCustomerId(customerId);
    }

    //getting all order of customer successfully
    @Test
    void testGetAllOrdersByCustomerId_whenNoOrdersExist() {
        // Arrange
        Long customerId = 2L;

        when(orderService.getAllOrdersByCustomerId(customerId)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<OrderResponseDTO>> response = customerController.getAllOrdersByCustomerId(customerId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(orderService, times(1)).getAllOrdersByCustomerId(customerId);
    }

}
