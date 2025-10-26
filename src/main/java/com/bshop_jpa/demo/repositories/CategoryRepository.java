package com.bshop_jpa.demo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    boolean existsByName(String name);
    List<Category> findAll();
}
