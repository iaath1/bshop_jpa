package com.bshop_jpa.demo.repositories;



import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.Status;

public interface StatusRepository extends CrudRepository<Status, Integer>{
    boolean existsByName(String name);
    Optional<Status> findByName(String name);
    List<Status> findAll();
}
