package com.bshop_jpa.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.User;

public interface UserRepository extends CrudRepository<User, Long>{
    
}
