package com.hcltech.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcltech.dto.CustomerRequestDTO;
import com.hcltech.dto.CustomerResponseDTO;
import com.hcltech.model.Customer;
import com.hcltech.repository.CustomerRepository;
import com.hcltech.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRespository;

    @Autowired
    private ModelMapper modelMapper;

//	@Autowired
//	private PasswordEncoder passwordEncoder;

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO) {
//    	customerRequestDTO.setCustomerPassword(passwordEncoder.encode(customerRequestDTO.getCustomerPassword()));

        Customer customer =modelMapper.map(customerRequestDTO, Customer.class);
        Customer savedCustomer = customerRespository.save(customer);
        return modelMapper.map(savedCustomer, CustomerResponseDTO.class);
    }


//    @Override
//    public CustomerResponseDTO updateCustomer(Long customerId, CustomerRequestDTO customerRequestDTO) {
//        Customer customer = customerRespository.findById(customerId)
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//        customer.setCustomerName(customerRequestDTO.getCustomerName());
//        customer.setCustomerEmail(customerRequestDTO.getCustomerEmail());
//        customer.setCustomerPhone(customerRequestDTO.getCustomerPhone());
//
//        if (customerRequestDTO.getCustomerPassword() != null && !customerRequestDTO.getCustomerPassword().isEmpty()) {
//            customer.setCustomerPassword(passwordEncoder.encode(customerRequestDTO.getCustomerPassword()));
//        }
//
//        Customer updatedCustomer = customerRespository.save(customer);
//        return modelMapper.map(updatedCustomer, CustomerResponseDTO.class);
//    }

    @Override
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO customerRequestDTO) {
        Customer customer = customerRespository.findById(id).orElseThrow(
                ()-> new RuntimeException("Customer Not Found")
        );
        modelMapper.map(customerRequestDTO,Customer.class);

        return modelMapper.map(customerRespository.save(customer), CustomerResponseDTO.class);

    }

    @Override
    public void deleteCustomer(Long customerId) {
        Customer customer = customerRespository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customerRespository.delete(customer);
    }

    @Override
    public List<CustomerResponseDTO> getAllCutomers() {
        List<Customer> customers = customerRespository.findAll();
        return customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long customerId) {
        Customer customer = customerRespository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return modelMapper.map(customer, CustomerResponseDTO.class);
    }

}