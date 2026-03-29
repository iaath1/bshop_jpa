package com.bshop_jpa.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.OrderItem;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
    
}
