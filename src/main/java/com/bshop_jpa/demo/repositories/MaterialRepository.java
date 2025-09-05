package com.bshop_jpa.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bshop_jpa.demo.models.Material;

@Repository
public interface MaterialRepository extends CrudRepository<Material, Integer>{
    boolean existsByName(String name);
}
