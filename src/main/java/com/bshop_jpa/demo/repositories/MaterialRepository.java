package com.bshop_jpa.demo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bshop_jpa.demo.models.Material;

@Repository
public interface MaterialRepository extends CrudRepository<Material, Integer>{
    boolean existsByNameUaAndNamePl(String nameUa, String namePl);
    List<Material> findAll();
}
