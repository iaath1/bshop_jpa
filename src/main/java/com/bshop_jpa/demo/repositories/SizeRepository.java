package com.bshop_jpa.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.Size;

public interface SizeRepository extends CrudRepository<Size, Integer> {
    
}
