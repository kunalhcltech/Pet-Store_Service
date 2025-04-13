package com.hcltech.service;

import com.hcltech.dto.CustomerRequestDTO;
import com.hcltech.dto.CustomerResponseDTO;
import com.hcltech.exceptions.CustomerNotFoundException;
import com.hcltech.model.Customer;
import com.hcltech.repository.CustomerRepository;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    //    @Test
//    void createCustomer_Success() {
//        CustomerRequestDTO requestDTO = CustomerRequestDTO.builder()
//                .customerName("John")
//                .customerEmail("john@example.com")
//                .customerPhone("9999999999")
//                .customerPassword("pass123")
//                .build();
//
//        String encodedPass = "encodedPass123";
//        when(passwordEncoder.encode(requestDTO.getCustomerPassword())).thenReturn(encodedPass);
//
//        Customer customer = new Customer();
//        customer.setCustomerName("John");
//        customer.setCustomerEmail("john@example.com");
//        customer.setCustomerPhone("9999999999");
//        customer.setPassword(encodedPass);
//
//        Customer savedCustomer = new Customer();
//        savedCustomer.setCustomerId(1L);
//        savedCustomer.setCustomerName("John");
//        savedCustomer.setCustomerEmail("john@example.com");
//        savedCustomer.setCustomerPhone("9999999999");
//        savedCustomer.setPassword(encodedPass);
//
//        CustomerResponseDTO responseDTO = CustomerResponseDTO.builder()
//                .customerId(1L)
//                .customerName("John")
//                .customerEmail("john@example.com")
//                .customerPhone("9999999999")
//                .build();
//
//        when(modelMapper.map(requestDTO, Customer.class)).thenReturn(customer);
//        when(customerRepository.save(customer)).thenReturn(savedCustomer);
//        when(modelMapper.map(savedCustomer, CustomerResponseDTO.class)).thenReturn(responseDTO);
//
//        CustomerResponseDTO result = customerService.createCustomer(requestDTO);
//
//        assertNotNull(result);
//        assertEquals("John", result.getCustomerName());
//        verify(passwordEncoder).encode(requestDTO.getCustomerPassword());
//        verify(customerRepository).save(customer);
//    }
//
    @Test
    void updateCustomer_Success() {
        Long customerId = 1L;

        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(customerId);
        existingCustomer.setCustomerName("Old Name");

        CustomerRequestDTO requestDTO = CustomerRequestDTO.builder()
                .customerName("New Name")
                .customerEmail("new@example.com")
                .customerPhone("1234567890")
                .build();

        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerId(customerId);
        updatedCustomer.setCustomerName("New Name");
        updatedCustomer.setCustomerEmail("new@example.com");
        updatedCustomer.setCustomerPhone("1234567890");

        CustomerResponseDTO responseDTO = CustomerResponseDTO.builder()
                .customerId(customerId)
                .customerName("New Name")
                .customerEmail("new@example.com")
                .customerPhone("1234567890")
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        doAnswer(invocation -> {
            CustomerRequestDTO dto = invocation.getArgument(0);
            Customer cust = invocation.getArgument(1);
            cust.setCustomerName(dto.getCustomerName());
            cust.setCustomerEmail(dto.getCustomerEmail());
            cust.setCustomerPhone(dto.getCustomerPhone());
            return null;
        }).when(modelMapper).map(eq(requestDTO), eq(existingCustomer));
        when(customerRepository.save(existingCustomer)).thenReturn(updatedCustomer);
        when(modelMapper.map(updatedCustomer, CustomerResponseDTO.class)).thenReturn(responseDTO);

        CustomerResponseDTO result = customerService.updateCustomer(customerId, requestDTO);

        assertNotNull(result);
        assertEquals("New Name", result.getCustomerName());
    }

    @Test
    void updateCustomer_NotFound() {
        Long id = 999L;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(id, new CustomerRequestDTO()));
    }

    @Test
    void deleteCustomer_Success() {
        Long id = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(id);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        String result = customerService.deleteCustomer(id);

        assertEquals("Customer Deleted Successfully id: 1", result);
        verify(customerRepository).delete(customer);
    }

    @Test
    void deleteCustomer_NotFound() {
        when(customerRepository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(123L));
    }

    @Test
    void getAllCustomers_Success() {
        Customer customer1 = new Customer();
        customer1.setCustomerId(1L);
        customer1.setCustomerName("John");

        Customer customer2 = new Customer();
        customer2.setCustomerId(2L);
        customer2.setCustomerName("Jane");

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        CustomerResponseDTO dto1 = CustomerResponseDTO.builder()
                .customerId(1L)
                .customerName("John")
                .build();
        CustomerResponseDTO dto2 = CustomerResponseDTO.builder()
                .customerId(2L)
                .customerName("Jane")
                .build();

        when(modelMapper.map(customer1, CustomerResponseDTO.class)).thenReturn(dto1);
        when(modelMapper.map(customer2, CustomerResponseDTO.class)).thenReturn(dto2);

        List<CustomerResponseDTO> result = customerService.getAllCutomers();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getCustomerName());
        assertEquals("Jane", result.get(1).getCustomerName());
    }

    @Test
    void getCustomerById_Success() {
        Long id = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(id);
        customer.setCustomerName("John");

        CustomerResponseDTO responseDTO = CustomerResponseDTO.builder()
                .customerId(id)
                .customerName("John")
                .build();

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(modelMapper.map(customer, CustomerResponseDTO.class)).thenReturn(responseDTO);

        CustomerResponseDTO result = customerService.getCustomerById(id);

        assertEquals("John", result.getCustomerName());
    }

    @Test
    void getCustomerById_NotFound() {
        when(customerRepository.findById(404L)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(404L));
    }
}