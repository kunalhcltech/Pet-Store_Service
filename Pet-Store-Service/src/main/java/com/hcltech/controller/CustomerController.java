package com.hcltech.controller;

import com.hcltech.dto.CustomerRequestDTO;
import com.hcltech.dto.CustomerResponseDTO;
import com.hcltech.dto.OrderResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer-api")
public class CustomerController {

    @PostMapping("/create")
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CustomerRequestDTO customerRequestDTO)
    {
        return null;
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable("customerId") Integer customerid)
    {
        return null;
    }

    @GetMapping("/get")
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers()
    {
        return null;
    }

    @GetMapping("/get/{customerId}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable("customerId") Integer customerid)
    {
        return null;
    }

    @GetMapping("/get/{customerId}")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrdersByCustomerId(@PathVariable("customerId") Integer customerid)
    {
        return null;
    }
}
