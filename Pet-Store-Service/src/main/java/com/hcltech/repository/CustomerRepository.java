package com.hcltech.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcltech.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByCustomerEmail(String email);
}