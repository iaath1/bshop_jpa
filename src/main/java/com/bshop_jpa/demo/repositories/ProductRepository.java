package com.bshop_jpa.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bshop_jpa.demo.DTO.CategoryCountDTO;
import com.bshop_jpa.demo.DTO.ColorCountDTO;
import com.bshop_jpa.demo.DTO.MaterialCountDTO;
import com.bshop_jpa.demo.DTO.SizeCountDTO;
import com.bshop_jpa.demo.models.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    
    @Query("SELECT new com.bshop_jpa.demo.DTO.CategoryCountDTO(p.category.name, COUNT(p)) " +
       "FROM Product p GROUP BY p.category.name")
    List<CategoryCountDTO> countProductsByCategory();

    @Query("SELECT new com.bshop_jpa.demo.DTO.SizeCountDTO(p.size.name, COUNT(p))" +
        "FROM Product p GROUP BY p.size.name")
    List<SizeCountDTO> countProductsBySize();

    @Query("SELECT new com.bshop_jpa.demo.DTO.ColorCountDTO(p.color.name, COUNT(p))" +
        "FROM Product p GROUP BY p.color.name")
    List<ColorCountDTO> countProductsByColor();

    @Query("SELECT new com.bshop_jpa.demo.DTO.MaterialCountDTO(p.material.name, COUNT(p))" +
        "FROM Product p GROUP BY p.material.name")
    List<MaterialCountDTO> countProductsByMaterial();
}
