package com.hcltech.repository;

import com.hcltech.model.Category;
import com.hcltech.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}