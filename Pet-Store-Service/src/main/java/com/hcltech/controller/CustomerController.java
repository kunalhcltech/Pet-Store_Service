package com.hcltech.controller;

import com.hcltech.dto.CustomerRequestDTO;
import com.hcltech.dto.CustomerResponseDTO;
import com.hcltech.dto.OrderResponseDTO;
import com.hcltech.service.CustomerService;
import com.hcltech.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer-api")
public class CustomerController {
    @Autowired
    private  CustomerService customerService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    @Operation(summary = "Add customer", description = "This method add's new customer")
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CustomerRequestDTO customerRequestDTO)
    {
        CustomerResponseDTO createdCustomer = customerService.createCustomer(customerRequestDTO);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
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
    @Operation(summary = "Get Specific Customer" , description = "This method retrieves customer with reference of ID")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable("customerId") Integer id)
    {
        CustomerResponseDTO customer = customerService.getCustomerById((long)(int)id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

   /* @GetMapping("/get/{customerId}")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrdersByCustomerId(@PathVariable("customer Id") Long customerid)
    {
        List<OrderResponseDTO> orders = orderService.getAllOrdersByCustomerId(customerId);
        if (orders.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Or HttpStatus.NOT_FOUND if you prefer
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }*/
}