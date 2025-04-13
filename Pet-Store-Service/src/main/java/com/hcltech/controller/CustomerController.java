package com.hcltech.controller;

import java.util.List;

import com.hcltech.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcltech.security.CustomerDetailsService;
import com.hcltech.security.JWTHelper;
import com.hcltech.service.CustomerService;
import com.hcltech.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/customer-api")
public class CustomerController {
    @Autowired
    private  CustomerService customerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private JWTHelper helper;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private CustomerDetailsService customerDetailsService;

    @PostMapping("/create")
    @Operation(summary = "Add customer", description = "This method add's new customer")
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CustomerRequestDTO customerRequestDTO)
    {
        CustomerResponseDTO createdCustomer = customerService.createCustomer(customerRequestDTO);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> login(@RequestBody JWTAuthRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = customerDetailsService.loadUserByUsername(request.getEmail());
        String token = helper.generateToken(userDetails);

        return ResponseEntity.ok(JWTAuthResponse.builder()
                .token(token)
                .username(userDetails.getUsername())
                .build());
    }


    @DeleteMapping("/delete/{customerId}")
    @Operation(summary ="Delete Customer" ,description = "This method delete's acustomer")
    public ResponseEntity<String> deleteCustomer(@PathVariable("customerId") Long id)
    {
        String deleteCustomer = customerService.deleteCustomer(id);
        return new ResponseEntity<>(deleteCustomer,HttpStatus.OK);
    }

    @GetMapping("/get")
    @Operation(summary ="Get all customers",description = "This method retrieves all the customers")
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers()
    {
        List<CustomerResponseDTO> allCustomers = customerService.getAllCutomers();
        return new ResponseEntity<>(allCustomers, HttpStatus.OK);
    }


    @GetMapping("/get/{customerId}")
    @Operation(summary = "Get Customer By ID" , description = "This method retrieves customer with reference of ID")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable("customerId") Integer id)
    {
        CustomerResponseDTO customer = customerService.getCustomerById((long)(int)id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/get/orders/{customerId}")
    @Operation(summary = "Get all order By customer ID" , description = "This method retrieves orders with reference of customerID")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrdersByCustomerId(@PathVariable("customer Id") Long customerId)
    {
        List<OrderResponseDTO> orders = orderService.getAllOrdersByCustomerId(customerId);
        if (orders.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Or HttpStatus.NOT_FOUND if you prefer
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}