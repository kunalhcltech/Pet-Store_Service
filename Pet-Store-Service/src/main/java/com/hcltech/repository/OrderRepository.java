package com.hcltech.repository;

import com.hcltech.model.Category;
import com.hcltech.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByCustomer_customerId(Long customerId);
}