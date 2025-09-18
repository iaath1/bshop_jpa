package com.bshop_jpa.demo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.Cart;
import com.bshop_jpa.demo.models.User;

public interface CartRepository extends CrudRepository<Cart, Long>{
    Optional<Cart> findByUser(User user);
}
