package com.bshop_jpa.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.CartItem;

public interface CartItemRepository extends CrudRepository<CartItem, Long>{
    
}
