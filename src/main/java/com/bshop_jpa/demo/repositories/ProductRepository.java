package com.bshop_jpa.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
    
}
