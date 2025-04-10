package com.hcltech.controller;

import com.hcltech.dto.CustomerRequestDTO;
import com.hcltech.dto.CustomerResponseDTO;
import com.hcltech.dto.OrderResponseDTO;
import com.hcltech.service.CustomerService;
import com.hcltech.service.OrderService;
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
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CustomerRequestDTO customerRequestDTO)
    {
        CustomerResponseDTO createdCustomer = customerService.createCustomer(customerRequestDTO);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id)
    {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/get")
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers()
    {
            List<CustomerResponseDTO> allCustomers = customerService.getAllCutomers();
            return new ResponseEntity<>(allCustomers, HttpStatus.OK);
    }


    @GetMapping("/get/{customerId}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id)
    {
        CustomerResponseDTO customer = customerService.getCustomerById(id);
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
