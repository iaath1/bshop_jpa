package com.bshop_jpa.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bshop_jpa.demo.models.Size;

@Repository
public interface SizeRepository extends CrudRepository<Size, Integer> {
    
}
