package com.bshop_jpa.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.Order;

public interface OrderRepository extends CrudRepository<Order, Long>{
    List<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
