package com.bshop_jpa.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.Role;

public interface RoleRepository extends CrudRepository<Role, Integer>{
    
}
