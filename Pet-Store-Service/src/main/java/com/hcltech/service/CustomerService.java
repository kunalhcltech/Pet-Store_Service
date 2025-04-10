package com.hcltech.service;

import java.util.List;

import com.hcltech.dto.CustomerRequestDTO;

import com.hcltech.dto.CustomerResponseDTO;

public interface CustomerService {

    CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO);

    CustomerResponseDTO updateCustomer(Long customerId,CustomerRequestDTO customerRequestDTO);

    String deleteCustomer(Long customerId);

    List<CustomerResponseDTO> getAllCutomers();

    CustomerResponseDTO getCustomerById(Long customerId);

}

