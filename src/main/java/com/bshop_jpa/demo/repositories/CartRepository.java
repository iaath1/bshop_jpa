package com.bshop_jpa.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.Cart;

public interface CartRepository extends CrudRepository<Cart, Long>{
    
}
