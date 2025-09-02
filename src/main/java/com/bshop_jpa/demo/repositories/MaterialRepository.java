package com.bshop_jpa.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.Material;

public interface MaterialRepository extends CrudRepository<Material, Integer>{
    
}
