package com.bshop_jpa.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.DTO.StatusCountDTO;
import com.bshop_jpa.demo.models.Order;

public interface OrderRepository extends CrudRepository<Order, Long>{
    List<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT new com.bshop_jpa.demo.DTO.StatusCountDTO(o.status.name, COUNT(o))" +
        "FROM Order o GROUP BY o.status.name")
    List<StatusCountDTO> countProductsByStatus();
}
