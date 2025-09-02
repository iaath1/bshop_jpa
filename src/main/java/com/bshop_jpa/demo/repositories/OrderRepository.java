package com.bshop_jpa.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.Order;

public interface OrderRepository extends CrudRepository<Order, Long>{
    
}
