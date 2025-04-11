package com.hcltech.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // ✅ Logger import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcltech.dto.CustomerRequestDTO;
import com.hcltech.dto.CustomerResponseDTO;
import com.hcltech.exceptions.CustomerNotFoundException;
import com.hcltech.model.Customer;
import com.hcltech.repository.CustomerRepository;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class); // ✅ Logger

    @Autowired
    private CustomerRepository customerRespository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO) {
        logger.info("Creating new customer with email: ", customerRequestDTO.getCustomerEmail());

        customerRequestDTO.setCustomerPassword(passwordEncoder.encode(customerRequestDTO.getCustomerPassword()));
        Customer customer = modelMapper.map(customerRequestDTO, Customer.class);
        Customer savedCustomer = customerRespository.save(customer);

        logger.debug("Customer saved successfully: ", savedCustomer);
        return modelMapper.map(savedCustomer, CustomerResponseDTO.class);
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO customerRequestDTO) {
        logger.info("Updating customer with ID: ", id);

        Customer customer = customerRespository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not Found"));

        modelMapper.map(customerRequestDTO, customer);

        Customer updated = customerRespository.save(customer);
        logger.debug("Customer updated successfully: ", updated);

        return modelMapper.map(updated, CustomerResponseDTO.class);
    }

    @Override
    public String deleteCustomer(Long customerId) {
        logger.info("Deleting customer with ID: ", customerId);

        Customer customer = customerRespository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        customerRespository.delete(customer);
        logger.info("Customer deleted with ID: ", customerId);

        return "Customer Deleted Successfully id: " + customerId;
    }

    @Override
    public List<CustomerResponseDTO> getAllCutomers() {
        logger.info("Fetching all customers");

        List<Customer> customers = customerRespository.findAll();

        logger.debug("Total customers fetched: ", customers.size());
        return customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long customerId) {
        logger.info("Fetching customer by ID: ", customerId);

        Customer customer = customerRespository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        logger.debug("Customer found: ", customer);
        return modelMapper.map(customer, CustomerResponseDTO.class);
    }
}
