package com.bshop_jpa.demo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.Role;

public interface RoleRepository extends CrudRepository<Role, Integer>{
    Optional<Role> findByName(String name);

    boolean existsByName(String name);
}
