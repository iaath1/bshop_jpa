package com.bshop_jpa.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.Color;

public interface ColorRepository extends CrudRepository<Color, Integer>{
    
}
