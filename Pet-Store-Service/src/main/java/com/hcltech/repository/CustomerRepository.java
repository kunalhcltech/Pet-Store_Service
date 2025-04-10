package com.hcltech.repository;

import com.hcltech.model.Category;
import com.hcltech.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}