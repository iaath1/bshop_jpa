package com.bshop_jpa.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    
}
