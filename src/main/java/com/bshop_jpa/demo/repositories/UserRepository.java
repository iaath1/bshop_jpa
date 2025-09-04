package com.bshop_jpa.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.DTO.RoleCountDTO;
import com.bshop_jpa.demo.models.User;

public interface UserRepository extends CrudRepository<User, Long>{

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT new com.bshop_jpa.demo.DTO.RoleCountDTO(r.name, COUNT(u)) " +
           "FROM User u JOIN u.roles r " +
           "GROUP BY r.name")
    List<RoleCountDTO> countUsersByRole();
}
