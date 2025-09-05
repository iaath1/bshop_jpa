package com.bshop_jpa.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bshop_jpa.demo.models.Color;

@Repository
public interface ColorRepository extends CrudRepository<Color, Integer>{
    boolean existsByName(String name);
}
