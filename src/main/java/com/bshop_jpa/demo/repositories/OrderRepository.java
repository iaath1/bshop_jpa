package com.bshop_jpa.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.DTO.StatusCountDTO;
import com.bshop_jpa.demo.models.Order;

public interface OrderRepository extends CrudRepository<Order, Long>{
    List<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT new com.bshop_jpa.demo.DTO.StatusCountDTO(s.name, COUNT(o)) " +
       "FROM Status s LEFT JOIN Order o ON o.status = s " +
       "GROUP BY s.name")
    List<StatusCountDTO> countProductsByStatus();
}
